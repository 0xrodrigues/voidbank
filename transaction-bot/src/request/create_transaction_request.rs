use rust_decimal::prelude::*;

#[derive(serde::Serialize, Debug)]
pub struct CreateTransactionRequest {
    pub from: i32,
    pub to: i32,
    pub transaction_type: String,
    pub amount: Decimal,
    pub comments: String,
}
