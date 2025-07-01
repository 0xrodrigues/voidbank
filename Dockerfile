# Multi-stage Dockerfile for VoidBank Application
# Stage 1: Build Java Backend
FROM maven:3.9.6-openjdk-17-slim AS java-builder

WORKDIR /app/backend

# Copy Maven files for dependency caching
COPY backend/pom.xml .
COPY backend/mvnw .
COPY backend/mvnw.cmd .
COPY backend/.mvn .mvn

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Build Rust Transaction Bot
FROM rust:1.75-slim AS rust-builder

# Install system dependencies for building
RUN apt-get update && apt-get install -y libsasl-dev libssl-dev libzstd-dev pkg-config && rm -rf /var/lib/apt/lists/*

WORKDIR /app/transaction-bot

# Copy Cargo files for dependency caching
COPY transaction-bot/Cargo.toml .
COPY transaction-bot/Cargo.lock .

# Create a dummy main.rs to build dependencies
RUN mkdir src && echo "fn main() {}" > src/main.rs

# Build dependencies (cached layer)
RUN cargo build --release
RUN rm src/main.rs

# Copy source code
COPY transaction-bot/src ./src

# Build the application
RUN cargo build --release

# Stage 3: Runtime image
FROM openjdk:17-jdk-slim

# Install runtime dependencies
RUN apt-get update && apt-get install -y curl netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Create application directory
WORKDIR /app

# Copy built applications
COPY --from=java-builder /app/backend/target/*.jar app.jar
COPY --from=rust-builder /app/transaction-bot/target/release/transaction-bot transaction-bot

# Create startup script
RUN echo '#!/bin/bash\n\
set -e\n\
\n\
# Wait for MySQL\n\
echo "Waiting for MySQL..."\n\
while ! nc -z mysql-db 3306; do\n\
  sleep 1\n\
done\n\
echo "MySQL is ready!"\n\
\n\
# Wait for Kafka\n\
echo "Waiting for Kafka..."\n\
while ! nc -z kafka 9092; do\n\
  sleep 1\n\
done\n\
echo "Kafka is ready!"\n\
\n\
# Start Java application in background\n\
echo "Starting Java backend..."\n\
java -jar app.jar &\n\
JAVA_PID=$!\n\
\n\
# Wait a bit for Java app to start\n\
sleep 10\n\
\n\
# Start Rust transaction bot\n\
echo "Starting Rust transaction bot..."\n\
./transaction-bot &\n\
RUST_PID=$!\n\
\n\
# Wait for any process to exit\n\
wait -n\n\
\n\
# Exit with status of process that exited first\n\
exit $?\n\
' > /app/start.sh && chmod +x /app/start.sh

# Expose port for Spring Boot
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start both applications
CMD ["/app/start.sh"]
