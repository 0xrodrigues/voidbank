use std::collections::HashMap;
use crate::request::create_transaction_request::CreateTransactionRequest;

pub async fn create_transaction(request: CreateTransactionRequest) {
    let client = reqwest::Client::new();

    let mut map = HashMap::new();
    map.insert("from", request.from);
    map.insert("to", request.to);
    map.insert("amount", request.amount.to_string());
    map.insert("comments", request.comments);

    let call_result = client
        .post("http://localhost:8080/api/transaction")
        .json(&map)
        .send()
        .await;

    match call_result {
        Ok(response) => {
            if response.status().is_success() {
                println!("✅ Transação realizada com sucesso.");
            } else {
                let status = response.status();
                let body = response.text().await.unwrap_or_else(|_| "Erro ao ler body".to_string());
                println!("⚠️ Erro da API (status {}): {}", status, body);
            }
        }
        Err(err) => {
            println!("❌ Erro ao enviar requisição: {:?}", err);
        }
    }
}