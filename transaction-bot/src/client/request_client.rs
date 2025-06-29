use crate::client::kafka_producer::send_to_kafka;
use crate::request::create_transaction_request::CreateTransactionRequest;
use log::{info, warn};
use reqwest::Error;
use serde_json::json;
use std::collections::HashMap;

const KAFKA_TOPIC: &str = "create.transaction.failed.event";

pub struct RequestClient {
    pub url: String,
}

impl RequestClient {
    pub fn new(url: String) -> RequestClient {
        RequestClient { url: url }
    }

    pub async fn send_create_transaction_request(self, request: CreateTransactionRequest) {
        info!("Creating a request to transaction-api {:?}", request);

        let client = reqwest::Client::new();

        let mut map: HashMap<&'static str, String> = HashMap::new();
        map.insert("from", request.from.to_string());
        map.insert("to", request.to.to_string());
        map.insert("amount", request.amount.to_string());
        map.insert("transactionType", request.transaction_type.clone());
        map.insert("comments", request.comments.clone());

        let call_result = client.post(self.url).json(&map).send().await;

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
                let payload = get_payload_error(request, err);
                if let Err(e) = send_to_kafka(KAFKA_TOPIC, &payload).await {
                    warn!("❌ Falha ao enviar evento para o Kafka: {:?}", e);
                } else {
                    info!("✅ Evento de falha enviado para o Kafka com sucesso.");
                }
            }
        }
    }
}

fn get_payload_error(request: CreateTransactionRequest, err: Error) -> String {
    json!({
        "error": format!("{:?}", err),
        "request": &request
    })
    .to_string()
}
