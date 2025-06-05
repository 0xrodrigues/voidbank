# VOIDBANK 🏦

Looking for a professional ecosystem to train and practice technology concepts?  
Use **VOIDBANK**, a fintech prototype written in Java that simulates a modern banking system based on a distributed systems architecture and event-driven communication using Kafka, MySQL, Dynatrace, Docker, and more.

> 🚀 Ideal for developers who want to explore backend technologies in a realistic, hands-on project.

---

## 🔍 Overview

VOIDBANK is a sandbox banking system designed for educational purposes.  
Its goal is to support developers studying and applying knowledge in:

- Java & Spring Boot
- Asynchronous event and concurrency tasks
- Dockerized environments
- Relational databases (MySQL)
- Observability with Dynatrace

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
