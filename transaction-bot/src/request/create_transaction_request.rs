use rust_decimal::prelude::*;

#[derive(serde::Serialize, Debug)]
pub struct CreateTransactionRequest {
    pub from: String,
    pub to: String,
    pub amount: Decimal,
    pub comments: String,
}
