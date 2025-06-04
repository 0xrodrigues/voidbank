use crate::request::create_transaction_request::CreateTransactionRequest;
use log::{info, warn};
use std::collections::HashMap;

pub async fn create_transaction(request: CreateTransactionRequest) {
    info!("Creating a request to transaction-api {:?}", request);

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
                info!("✅ Transação realizada com sucesso.");
            } else {
                let status = response.status();
                warn!("⚠️ Error on API (status {})", status);
            }
        }
        Err(err) => {
            warn!("❌ Erro ao enviar requisição: {:?}", err);
            // TODO: Jogar em um topico de exceção - create.transaction.failed.event
        }
    }
}
