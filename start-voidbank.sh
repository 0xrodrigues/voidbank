#!/bin/bash

# VoidBank Application Startup Script
set -e

echo "🏦 Starting VoidBank Application..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    print_error "docker-compose is not installed. Please install docker-compose and try again."
    exit 1
fi

# Parse command line arguments
COMMAND=${1:-"up"}
PROFILE=${2:-""}

case $COMMAND in
    "up")
        print_status "Building and starting VoidBank application..."
        if [ "$PROFILE" = "monitoring" ]; then
            print_status "Starting with monitoring tools (Kafka UI)..."
            docker-compose --profile monitoring up --build -d
        else
            docker-compose up --build -d
        fi
        
        print_success "VoidBank application is starting up!"
        print_status "Services will be available at:"
        echo "  🌐 Backend API: http://localhost:8080"
        if [ "$PROFILE" = "monitoring" ]; then
            echo "  📊 Kafka UI: http://localhost:8081"
        fi
        echo "  🗄️  MySQL: localhost:3306"
        echo "  📨 Kafka: localhost:9092"
        
        print_status "Checking service health..."
        sleep 5
        docker-compose ps
        ;;
        
    "down")
        print_status "Stopping VoidBank application..."
        docker-compose down
        print_success "VoidBank application stopped!"
        ;;
        
    "logs")
        SERVICE=${2:-""}
        if [ -z "$SERVICE" ]; then
            print_status "Showing logs for all services..."
            docker-compose logs -f
        else
            print_status "Showing logs for service: $SERVICE"
            docker-compose logs -f "$SERVICE"
        fi
        ;;
        
    "restart")
        print_status "Restarting VoidBank application..."
        docker-compose restart
        print_success "VoidBank application restarted!"
        ;;
        
    "clean")
        print_warning "This will remove all containers, volumes, and images related to VoidBank."
        read -p "Are you sure? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            print_status "Cleaning up VoidBank resources..."
            docker-compose down -v --rmi all
            docker system prune -f
            print_success "Cleanup completed!"
        else
            print_status "Cleanup cancelled."
        fi
        ;;
        
    "status")
        print_status "VoidBank application status:"
        docker-compose ps
        ;;
        
    "help"|*)
        echo "🏦 VoidBank Application Management Script"
        echo ""
        echo "Usage: $0 [COMMAND] [OPTIONS]"
        echo ""
        echo "Commands:"
        echo "  up [monitoring]    Start the application (add 'monitoring' for Kafka UI)"
        echo "  down              Stop the application"
        echo "  logs [service]    Show logs (optionally for specific service)"
        echo "  restart           Restart the application"
        echo "  status            Show application status"
        echo "  clean             Remove all containers, volumes, and images"
        echo "  help              Show this help message"
        echo ""
        echo "Examples:"
        echo "  $0 up                    # Start application"
        echo "  $0 up monitoring         # Start with Kafka UI"
        echo "  $0 logs voidbank-app     # Show app logs"
        echo "  $0 down                  # Stop application"
        ;;
esac
