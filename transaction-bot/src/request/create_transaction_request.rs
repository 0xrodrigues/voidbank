use rust_decimal::prelude::*;

#[derive(serde::Serialize)]
pub struct CreateTransactionRequest {
    pub from: String,
    pub to: String,
    pub amount: Decimal,
    pub comments: String
}