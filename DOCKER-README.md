# 🏦 VoidBank - Docker Setup

Este documento explica como executar a aplicação VoidBank usando Docker.

## 📋 Pré-requisitos

- Docker (versão 20.10 ou superior)
- Docker Compose (versão 2.0 ou superior)
- 4GB de RAM disponível
- 10GB de espaço em disco

## 🏗️ Arquitetura da Aplicação

A aplicação VoidBank é composta por:

- **Backend API** (Java 17 + Spring Boot 3.3.12)
- **Transaction Bot** (Rust 1.75)
- **MySQL 8.0** (Banco de dados)
- **Apache Kafka 3.6** (Message broker)
- **Kafka UI** (Interface de monitoramento - opcional)

## 🚀 Início Rápido

### Opção 1: Usando o script de inicialização (Recomendado)

```bash
# Iniciar a aplicação
./start-voidbank.sh up

# Iniciar com ferramentas de monitoramento
./start-voidbank.sh up monitoring

# Ver logs
./start-voidbank.sh logs

# Parar a aplicação
./start-voidbank.sh down
```

### Opção 2: Usando Docker Compose diretamente

```bash
# Construir e iniciar todos os serviços
docker-compose up --build -d

# Verificar status dos serviços
docker-compose ps

# Ver logs
docker-compose logs -f

# Parar todos os serviços
docker-compose down
```

## 🌐 Endpoints Disponíveis

Após iniciar a aplicação, os seguintes serviços estarão disponíveis:

- **Backend API**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **MySQL**: localhost:3306
- **Kafka**: localhost:9092
- **Kafka UI** (se habilitado): http://localhost:8081

## 📊 Monitoramento

### Health Checks

A aplicação inclui health checks para todos os serviços:

```bash
# Verificar saúde da aplicação
curl http://localhost:8080/actuator/health

# Verificar status dos containers
docker-compose ps
```

### Logs

```bash
# Logs de todos os serviços
docker-compose logs -f

# Logs de um serviço específico
docker-compose logs -f voidbank-app
docker-compose logs -f mysql-db
docker-compose logs -f kafka
```

## 🔧 Configuração

### Variáveis de Ambiente

As principais configurações podem ser alteradas no `docker-compose.yml`:

```yaml
environment:
  # Database
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql-db:3306/voidbank
  SPRING_DATASOURCE_USERNAME: root
  SPRING_DATASOURCE_PASSWORD: 123456
  
  # Kafka
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  
  # Java
  JAVA_OPTS: -Xmx512m -Xms256m
```

### Portas

| Serviço | Porta Host | Porta Container |
|---------|------------|-----------------|
| Backend API | 8080 | 8080 |
| MySQL | 3306 | 3306 |
| Kafka | 9092 | 9092 |
| Kafka UI | 8081 | 8080 |

## 🗄️ Banco de Dados

### Inicialização

Os scripts SQL em `./sql/` são executados automaticamente na inicialização do MySQL:

- `initial_create_table.sql` - Criação das tabelas
- `initial_insert_accounts.sql` - Dados iniciais

### Conexão Manual

```bash
# Conectar ao MySQL
docker exec -it voidbank-mysql mysql -u root -p123456 voidbank
```

## 📨 Kafka

### Tópicos

A aplicação usa o seguinte tópico:
- `voidbank.transaction.process.event`

### Kafka UI

Para habilitar a interface de monitoramento do Kafka:

```bash
./start-voidbank.sh up monitoring
```

Acesse: http://localhost:8081

## 🛠️ Desenvolvimento

### Build Local

```bash
# Build apenas da imagem da aplicação
docker-compose build voidbank-app

# Build forçado (sem cache)
docker-compose build --no-cache voidbank-app
```

### Debug

```bash
# Executar em modo interativo
docker-compose run --rm voidbank-app bash

# Verificar logs em tempo real
docker-compose logs -f voidbank-app
```

## 🧹 Limpeza

```bash
# Parar e remover containers
docker-compose down

# Remover volumes (CUIDADO: apaga dados do banco)
docker-compose down -v

# Limpeza completa usando o script
./start-voidbank.sh clean
```

## ⚠️ Troubleshooting

### Problemas Comuns

1. **Porta já em uso**
   ```bash
   # Verificar processos usando as portas
   lsof -i :8080
   lsof -i :3306
   lsof -i :9092
   ```

2. **Falta de memória**
   ```bash
   # Verificar uso de recursos
   docker stats
   ```

3. **Serviços não inicializam**
   ```bash
   # Verificar logs de erro
   docker-compose logs
   ```

### Comandos Úteis

```bash
# Reiniciar um serviço específico
docker-compose restart voidbank-app

# Executar comando no container
docker-compose exec voidbank-app bash

# Verificar recursos utilizados
docker system df
```

## 📝 Notas Importantes

- O primeiro build pode demorar alguns minutos
- Os dados do MySQL são persistidos em volumes Docker
- A aplicação aguarda MySQL e Kafka estarem prontos antes de iniciar
- O Transaction Bot inicia automaticamente após o Backend API

## 🤝 Suporte

Para problemas ou dúvidas:

1. Verifique os logs: `./start-voidbank.sh logs`
2. Verifique o status: `./start-voidbank.sh status`
3. Tente uma limpeza: `./start-voidbank.sh clean` e reinicie
