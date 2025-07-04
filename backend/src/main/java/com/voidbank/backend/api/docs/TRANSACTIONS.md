# 💸 API de Transações Financeiras

## 📋 Visão Geral

A API de Transações permite realizar transferências de fundos entre contas bancárias no sistema VoidBank. As transações são processadas de forma assíncrona via Apache Kafka.

**Base URL**: `/api/transaction`

## 📚 Endpoints Disponíveis

### 1. Realizar Transferência entre Contas

**`POST /api/transaction`**

Executa uma transferência de fundos entre duas contas bancárias do sistema VoidBank.

#### 📥 Request

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "from": 12345,
  "to": 67890,
  "amount": 150.75,
  "comments": "Pagamento de serviços",
  "type": "PIX"
}
```

#### 📤 Response

**Status: 200 OK**
```json
{
  "message": "Transaction completed successfully",
  "moment": "2024-01-15T10:30:00"
}
```

**Status: 400 Bad Request - Saldo Insuficiente**
```json
{
  "errorId": "550e8400-e29b-41d4-a716-446655440000",
  "error": "INSUFFICIENT_BALANCE",
  "errorMessage": "Saldo insuficiente para realizar a transação",
  "moment": "2024-01-15T10:30:00",
  "httpStatus": 400,
  "fieldErrors": []
}
```

**Status: 404 Not Found - Conta Não Encontrada**
```json
{
  "errorId": "550e8400-e29b-41d4-a716-446655440000",
  "error": "ACCOUNT_NOT_FOUND",
  "errorMessage": "Conta de origem ou destino não encontrada",
  "moment": "2024-01-15T10:30:00",
  "httpStatus": 404,
  "fieldErrors": []
}
```

#### 🔧 Regras de Negócio

- **Validação de contas**: Ambas as contas (origem e destino) devem existir
- **Saldo suficiente**: A conta de origem deve ter saldo suficiente
- **Processamento assíncrono**: Transações são enviadas para fila Kafka
- **Atomicidade**: Operação de débito e crédito são executadas em conjunto
- **Auditoria**: Todas as transações são logadas para rastreabilidade

#### 📝 Validações

| Campo | Tipo | Obrigatório | Validação |
|-------|------|-------------|-----------|
| from | Long | Sim | Conta de origem deve existir |
| to | Long | Sim | Conta de destino deve existir |
| amount | BigDecimal | Sim | Valor > 0, máximo 2 casas decimais |
| comments | String | Não | Máximo 255 caracteres |
| type | Enum | Sim | PIX ou TED |

#### ⚡ Tipos de Transação

| Tipo | Descrição | Características |
|------|-----------|-----------------|
| PIX | Transferência instantânea | Processamento imediato |
| TED | Transferência Eletrônica Disponível | Processamento em horário comercial |

---

## 🧪 Exemplos de Uso

### Usando cURL

#### Transferência PIX:
```bash
curl -X POST http://localhost:8080/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "from": 12345,
    "to": 67890,
    "amount": 250.00,
    "comments": "Transferência PIX para João",
    "type": "PIX"
  }'
```

#### Transferência TED:
```bash
curl -X POST http://localhost:8080/api/transaction \
  -H "Content-Type: application/json" \
  -d '{
    "from": 98765,
    "to": 54321,
    "amount": 1000.50,
    "comments": "Pagamento de fornecedor",
    "type": "TED"
  }'
```

### Usando JavaScript (Fetch API)

#### Realizar transferência:
```javascript
const transferFunds = async () => {
  const response = await fetch('http://localhost:8080/api/transaction', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      from: 12345,
      to: 67890,
      amount: 150.75,
      comments: 'Pagamento de aluguel',
      type: 'PIX'
    })
  });
  
  const result = await response.json();
  console.log(result);
};
```

#### Com tratamento de erro:
```javascript
const transferWithErrorHandling = async (transactionData) => {
  try {
    const response = await fetch('http://localhost:8080/api/transaction', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(transactionData)
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(`Erro ${error.httpStatus}: ${error.errorMessage}`);
    }
    
    const result = await response.json();
    console.log('Transação realizada:', result);
    return result;
    
  } catch (error) {
    console.error('Erro na transação:', error.message);
    throw error;
  }
};
```

### Usando Python (requests)

```python
import requests
import json

def transfer_funds(from_account, to_account, amount, comments, transaction_type):
    url = "http://localhost:8080/api/transaction"
    
    payload = {
        "from": from_account,
        "to": to_account,
        "amount": amount,
        "comments": comments,
        "type": transaction_type
    }
    
    headers = {
        "Content-Type": "application/json"
    }
    
    response = requests.post(url, data=json.dumps(payload), headers=headers)
    
    if response.status_code == 200:
        return response.json()
    else:
        error = response.json()
        raise Exception(f"Erro {error['httpStatus']}: {error['errorMessage']}")

# Exemplo de uso
try:
    result = transfer_funds(12345, 67890, 100.00, "Teste Python", "PIX")
    print("Sucesso:", result)
except Exception as e:
    print("Erro:", str(e))
```

## 🔍 Modelos de Dados

### CreateTransactionRequest
```json
{
  "from": "number",          // Conta de origem (obrigatório)
  "to": "number",            // Conta de destino (obrigatório)
  "amount": "decimal",       // Valor da transferência (obrigatório)
  "comments": "string",      // Comentários opcionais
  "type": "enum"             // PIX | TED (obrigatório)
}
```

### MessageResponseApi
```json
{
  "message": "string",       // Mensagem de sucesso
  "moment": "datetime"       // Timestamp da operação
}
```

### ErrorResponse
```json
{
  "errorId": "string",       // ID único do erro
  "error": "string",         // Código do erro
  "errorMessage": "string",  // Mensagem descritiva
  "moment": "datetime",      // Timestamp do erro
  "httpStatus": "number",    // Status HTTP
  "fieldErrors": "array"     // Erros de validação
}
```

## 🔄 Fluxo de Processamento

1. **Validação**: Sistema valida dados da requisição
2. **Verificação de contas**: Confirma existência das contas origem e destino
3. **Verificação de saldo**: Confirma saldo suficiente na conta origem
4. **Envio para Kafka**: Transação é enviada para processamento assíncrono
5. **Processamento**: Sistema processa débito e crédito atomicamente
6. **Confirmação**: Resposta de sucesso é retornada ao cliente

## ⚠️ Notas Importantes

- **Processamento assíncrono**: Transações são processadas via Kafka
- **Atomicidade**: Débito e crédito são executados como operação única
- **Validação rigorosa**: Sistema valida contas e saldo antes do processamento
- **Logs detalhados**: Todas as transações são auditadas
- **Idempotência**: Recomenda-se implementar controle de duplicação no cliente
- **Limites**: Não há limites de valor implementados atualmente

## 🔗 Links Relacionados

- [Visão Geral da API](API_OVERVIEW.md)
- [API de Contas](ACCOUNTS.md)
- [Swagger UI](http://localhost:8080/swagger-ui.html)
