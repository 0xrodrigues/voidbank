# 🏦 API de Contas Bancárias

## 📋 Visão Geral

A API de Contas permite o gerenciamento completo de contas bancárias no sistema VoidBank, incluindo criação e consulta de contas.

**Base URL**: `/api/account`

## 📚 Endpoints Disponíveis

### 1. Criar Nova Conta Bancária

**`POST /api/account`**

Cria uma nova conta bancária no sistema VoidBank com os dados fornecidos.

#### 📥 Request

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "ownerName": "João Silva Santos",
  "document": "12345678901",
  "documentType": "CPF"
}
```

#### 📤 Response

**Status: 200 OK**
```json
{
  "message": "Account created successfully",
  "moment": "2024-01-15T10:30:00"
}
```

**Status: 400 Bad Request**
```json
{
  "errorId": "550e8400-e29b-41d4-a716-446655440000",
  "error": "ACCOUNT_WITH_DOCUMENT_ALREADY_EXISTS",
  "errorMessage": "Já existe uma conta com este documento",
  "moment": "2024-01-15T10:30:00",
  "httpStatus": 400,
  "fieldErrors": []
}
```

#### 🔧 Regras de Negócio

- **Documento único**: Não é permitido criar contas com documentos já cadastrados
- **Geração automática**: Número da conta e dígito são gerados automaticamente
- **Agência padrão**: Todas as contas são criadas na agência 100
- **Saldo inicial**: Todas as contas iniciam com saldo zero

#### 📝 Validações

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| ownerName | String | Sim | Não pode ser vazio |
| document | String | Sim | Apenas números, documento válido |
| documentType | Enum | Sim | CPF ou RG |

---

### 2. Buscar Conta por ID

**`GET /api/account/{accountId}`**

Retorna os dados completos de uma conta bancária específica usando o número da conta.

#### 📥 Request

**Path Parameters:**
- `accountId` (Long): Número da conta bancária

**Headers:**
```
Content-Type: application/json
```

#### 📤 Response

**Status: 200 OK**
```json
{
  "nuAccount": 12345,
  "digit": 7,
  "agency": 100,
  "ownerName": "João Silva Santos",
  "document": "12345678901",
  "balance": 1500.75,
  "documentType": "CPF",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:30"
}
```

**Status: 404 Not Found**
```json
{
  "errorId": "550e8400-e29b-41d4-a716-446655440000",
  "error": "ACCOUNT_NOT_FOUND",
  "errorMessage": "Conta não encontrada",
  "moment": "2024-01-15T10:30:00",
  "httpStatus": 404,
  "fieldErrors": []
}
```

#### 🔧 Regras de Negócio

- **Busca por ID**: Utiliza o número da conta como identificador único
- **Dados completos**: Retorna todas as informações da conta
- **Saldo atual**: Mostra o saldo atualizado da conta

---

## 🧪 Exemplos de Uso

### Usando cURL

#### Criar uma nova conta:
```bash
curl -X POST http://localhost:8080/api/account \
  -H "Content-Type: application/json" \
  -d '{
    "ownerName": "Maria Silva Santos",
    "document": "98765432100",
    "documentType": "CPF"
  }'
```

#### Buscar conta por ID:
```bash
curl -X GET http://localhost:8080/api/account/12345 \
  -H "Content-Type: application/json"
```

### Usando JavaScript (Fetch API)

#### Criar conta:
```javascript
const createAccount = async () => {
  const response = await fetch('http://localhost:8080/api/account', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      ownerName: 'Pedro Santos',
      document: '11122233344',
      documentType: 'CPF'
    })
  });
  
  const result = await response.json();
  console.log(result);
};
```

#### Buscar conta:
```javascript
const getAccount = async (accountId) => {
  const response = await fetch(`http://localhost:8080/api/account/${accountId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    }
  });
  
  const account = await response.json();
  console.log(account);
};
```

## 🔍 Modelos de Dados

### CreateAccountRequest
```json
{
  "ownerName": "string",     // Nome completo do titular
  "document": "string",      // CPF/RG apenas números
  "documentType": "enum"     // CPF | RG
}
```

### AccountResponseDto
```json
{
  "nuAccount": "number",     // Número da conta
  "digit": "number",         // Dígito verificador
  "agency": "number",        // Número da agência
  "ownerName": "string",     // Nome do titular
  "document": "string",      // Documento do titular
  "balance": "decimal",      // Saldo atual
  "documentType": "enum",    // Tipo do documento
  "createdAt": "datetime",   // Data de criação
  "updatedAt": "datetime"    // Última atualização
}
```

## ⚠️ Notas Importantes

- **Processamento síncrono**: Criação de contas é processada imediatamente
- **Numeração automática**: O sistema gera automaticamente o número da conta
- **Validação de documento**: Sistema verifica se o documento já está cadastrado
- **Logs detalhados**: Todas as operações são logadas para auditoria
- **Thread-safe**: Operações são seguras para execução concorrente

## 🔗 Links Relacionados

- [Visão Geral da API](API_OVERVIEW.md)
- [API de Transações](TRANSACTIONS.md)
- [Swagger UI](http://localhost:8080/swagger-ui.html)
