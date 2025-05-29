# VOIDBANK 🏦

Looking for a professional ecosystem to train and practice technology concepts?  
Use **VOIDBANK**, a fintech prototype written in Java that simulates a modern banking system based on a distributed systems architecture and event-driven communication using Kafka, MySQL, Dynatrace, Docker, and more.

> 🚀 Ideal for developers who want to explore backend technologies in a realistic, hands-on project.

---

## 🔍 Overview

VOIDBANK is a sandbox banking system designed for educational purposes.  
Its goal is to support developers studying and applying knowledge in:

- Distributed Systems
- Event-Driven Architecture
- Java & Spring Boot
- Kafka (asynchronous messaging)
- Dockerized environments
- Relational databases (MySQL)
- Observability with Dynatrace
- Microservices communication patterns

This project offers a robust foundation for experiments, extensions, and learning.

---

## 📦 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Apache Kafka**
- **MySQL**
- **Docker & Docker Compose**
- **Dynatrace** (monitoring and observability)
- **Maven** (build management)

---

## 🧩 Architecture

VOIDBANK follows a **modular and distributed design**:

- `account-service`: Manages bank accounts
- `transaction-service`: Handles money transfers
- `kafka-producer/consumer`: Event communication between services
- `mysql-db`: Stores account and transaction data
- `dynatrace-agent`: Monitors application health and performance

Each module runs in its own container using Docker Compose, simulating a real-world distributed environment.

---

## ⚙️ Getting Started

### Prerequisites

- Docker + Docker Compose
- Java 17+
- Maven 3.8+

### Running the Project

```bash
git clone https://github.com/0xrodrigues/voidbank.git
cd voidbank
mvn clean install
```
For running tools to run you application:
```bash
cd voidbank/docker-lab
docker-compose -f docker-compose.kafka.yml up -d
docker-compose -f docker-compose.mysql.yml up -d 
```

---

## 🧪 Use Cases Simulated

- 📥 Account creation and balance updates  
- 💸 Money transfer between accounts  
- 🔄 Kafka-based asynchronous event handling  
- 📊 Transaction traceability through monitoring

---

## 🤝 Contribution

VOIDBANK is open for contributions!

Whether you want to:
- Add new features
- Refactor modules
- Enhance observability
- Expand business logic

You're welcome to open issues, fork the repo, and send pull requests.

---

## 📚 Learning Scenarios

This project is especially useful for:

- Practicing microservices communication with Kafka
- Containerizing services using Docker
- Experimenting with observability tools (like Dynatrace)
- Applying clean architecture principles in Java

---

## 🔗 Links

- 📘 Repository: [github.com/0xrodrigues/voidbank](https://github.com/0xrodrigues/voidbank)
- 🧑‍💻 Author: [@0xrodrigues](https://github.com/0xrodrigues)

---

## 📄 License

MIT License – see [`LICENSE`](./LICENSE) file for details.
