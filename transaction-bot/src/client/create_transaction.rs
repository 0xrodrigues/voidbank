use crate::request::create_transaction_request::CreateTransactionRequest;
use std::collections::HashMap;

pub async fn create_transaction(request: CreateTransactionRequest) {
    let client = reqwest::Client::new();

    let mut map = HashMap::new();

    map.insert("from", request.from);
    map.insert("to", request.to);
    map.insert("amount", request.amount.to_string());
    map.insert("comments", request.comments);

    let call = client.post("http://localhost:8080/api/transaction").json(&map).send().await;

    match call {
        Ok(r) => println!("Response: {:?}", r),
        Err(e) => println!("Error: {:?}", e)
    };

}