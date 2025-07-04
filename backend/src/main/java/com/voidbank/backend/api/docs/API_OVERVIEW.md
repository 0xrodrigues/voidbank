# 🏦 VoidBank API - Visão Geral

## 📋 Introdução

A API VoidBank é um sistema bancário RESTful desenvolvido em Spring Boot que oferece funcionalidades completas para gerenciamento de contas bancárias e transações financeiras.

## 🚀 Acesso Rápido

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml
- **Health Check**: http://localhost:8080/actuator/health

## 🏗️ Arquitetura da API

### Padrão de Design
A API segue o padrão de **Interface Segregation** onde:
- **Interfaces API** (`/api` package): Contêm toda a documentação Swagger
- **Controllers**: Implementam as interfaces focando apenas na lógica de negócio
- **Services**: Contêm a lógica de negócio
- **Repositories**: Gerenciam o acesso aos dados

### Estrutura de Pacotes
```
com.voidbank.backend.api/
├── docs/                    # Documentação detalhada
│   ├── API_OVERVIEW.md     # Este arquivo
│   ├── ACCOUNTS.md         # Documentação de contas
│   └── TRANSACTIONS.md     # Documentação de transações
├── AccountApi.java         # Interface com doc Swagger para contas
└── TransactionApi.java     # Interface com doc Swagger para transações
```

## 📚 Recursos Disponíveis

### 🏦 Contas Bancárias (`/api/account`)
- **Criação de contas**: Cadastro de novas contas bancárias
- **Consulta de contas**: Busca detalhada por ID da conta

### 💸 Transações (`/api/transaction`)
- **Transferências**: Movimentação de fundos entre contas
- **Processamento assíncrono**: Via Apache Kafka

## 🔧 Tecnologias Utilizadas

- **Spring Boot 3.3.12** - Framework principal
- **Java 17** - Linguagem de programação
- **SpringDoc OpenAPI 2.2.0** - Documentação Swagger
- **MySQL** - Banco de dados
- **Apache Kafka** - Processamento assíncrono
- **Docker** - Containerização
- **Maven** - Gerenciamento de dependências

## 📖 Como Usar Esta Documentação

1. **Visão Geral** (este arquivo): Entenda a estrutura geral da API
2. **[ACCOUNTS.md](ACCOUNTS.md)**: Documentação detalhada dos endpoints de contas
3. **[TRANSACTIONS.md](TRANSACTIONS.md)**: Documentação detalhada dos endpoints de transações
4. **Swagger UI**: Interface interativa para testar os endpoints

## 🔐 Autenticação

Atualmente a API não possui autenticação implementada. Todos os endpoints são públicos.

## 📊 Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | Operação realizada com sucesso |
| 400 | Dados inválidos na requisição |
| 404 | Recurso não encontrado |
| 500 | Erro interno do servidor |

## 🚨 Tratamento de Erros

Todos os erros seguem o padrão `ErrorResponse`:

```json
{
  "errorId": "uuid-do-erro",
  "error": "CODIGO_DO_ERRO",
  "errorMessage": "Descrição detalhada do erro",
  "moment": "2024-01-15T10:30:00",
  "httpStatus": 400,
  "fieldErrors": [
    {
      "field": "campo",
      "message": "mensagem de erro"
    }
  ]
}
```

## 🧪 Ambiente de Desenvolvimento

### Pré-requisitos
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional)

### Executando a Aplicação
```bash
# Via Maven
mvn spring-boot:run

# Via Docker
docker-compose up
```

## 📞 Suporte

Para dúvidas ou problemas:
- Consulte a documentação específica de cada recurso
- Use o Swagger UI para testes interativos
- Verifique os logs da aplicação para debugging
