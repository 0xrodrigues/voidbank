# 📚 VoidBank API Documentation

Este documento descreve a API REST do VoidBank, um sistema bancário para gerenciamento de contas e transações.

## 🚀 Acesso Rápido

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

## 📋 Visão Geral

A API VoidBank oferece endpoints para:
- Criação de contas bancárias
- Realização de transações entre contas
- Gerenciamento de transferências

## 🏗️ Arquitetura da API

A documentação Swagger está organizada usando interfaces dedicadas:
- **AccountApi**: Interface com documentação para operações de contas
- **TransactionApi**: Interface com documentação para operações de transações

Os controllers implementam essas interfaces, mantendo a separação entre documentação e lógica de negócio.

## 🔗 Endpoints

### Contas Bancárias

#### `POST /api/account`
Cria uma nova conta bancária no sistema.

**Request Body:**
```json
{
  "ownerName": "João Silva Santos",
  "document": "12345678901",
  "documentType": "CPF"
}
```

**Response (200 OK):**
```json
{
  "message": "Account created successfully",
  "moment": "2024-01-15T10:30:00"
}
```

**Possíveis Erros:**
- `400 Bad Request` - Dados inválidos ou conta já existe
- `500 Internal Server Error` - Erro interno do servidor

### Transações

#### `POST /api/transaction`
Realiza uma transferência entre duas contas bancárias.

**Request Body:**
```json
{
  "from": 12345,
  "to": 67890,
  "amount": 100.50,
  "comments": "Pagamento de serviços",
  "type": "PIX"
}
```

**Response (200 OK):**
```json
{
  "message": "Transaction completed successfully",
  "moment": "2024-01-15T10:30:00"
}
```

**Possíveis Erros:**
- `400 Bad Request` - Dados inválidos ou saldo insuficiente
- `404 Not Found` - Conta não encontrada
- `500 Internal Server Error` - Erro interno do servidor

## 📊 Modelos de Dados

### CreateAccountRequest
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| ownerName | string | Sim | Nome completo do titular |
| document | string | Sim | CPF ou CNPJ |
| documentType | enum | Sim | CPF ou CNPJ |

### CreateTransactionRequest
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| from | number | Sim | Conta de origem |
| to | number | Sim | Conta de destino |
| amount | number | Sim | Valor da transação |
| comments | string | Não | Observações |
| type | enum | Sim | Tipo da transação |

### MessageResponseApi
| Campo | Tipo | Descrição |
|-------|------|-----------|
| message | string | Mensagem de sucesso |
| moment | datetime | Timestamp da resposta |

### ErrorResponse
| Campo | Tipo | Descrição |
|-------|------|-----------|
| errorId | string | ID único do erro |
| error | string | Código do erro |
| errorMessage | string | Mensagem descritiva |
| moment | datetime | Timestamp do erro |
| httpStatus | number | Status HTTP |
| fieldErrors | array | Erros de validação |

## 🔧 Tipos Enumerados

### DocumentType
- `CPF` - Cadastro de Pessoa Física
- `CNPJ` - Cadastro Nacional de Pessoa Jurídica

### TransactionType
- `PIX` - Transferência instantânea
- `TED` - Transferência Eletrônica Disponível
- `DOC` - Documento de Ordem de Crédito

## 🛠️ Testando a API

### Usando Swagger UI
1. Acesse http://localhost:8080/swagger-ui.html
2. Selecione o endpoint desejado
3. Clique em "Try it out"
4. Preencha os dados necessários
5. Execute a requisição

### Usando cURL

**Criar conta:**
```bash
curl -X POST http://localhost:8080/api/account \
  -H "Content-Type: application/json" \
  -d '{
    "ownerName": "João Silva",
    "document": "12345678901",
    "documentType": "CPF"
  }'
```

**Realizar transação:**
```bash
curl -X POST http://localhost:8080/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "from": 12345,
    "to": 67890,
    "amount": 100.50,
    "comments": "Teste",
    "type": "PIX"
  }'
```

## 🔍 Códigos de Status

| Código | Descrição |
|--------|-----------|
| 200 | Operação realizada com sucesso |
| 400 | Dados inválidos na requisição |
| 404 | Recurso não encontrado |
| 500 | Erro interno do servidor |

## 📝 Notas Importantes

- Todas as requisições devem usar `Content-Type: application/json`
- Valores monetários devem ser enviados como números decimais
- Documentos devem conter apenas números
- O sistema valida a existência das contas antes de realizar transações
- Transações são processadas de forma assíncrona via Kafka

## 🔗 Links Úteis

- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [Health Check](http://localhost:8080/actuator/health)
- [Repositório GitHub](https://github.com/0xrodrigues/voidbank)
