mod request;
mod client;

use rust_decimal::Decimal;
use crate::request::create_transaction_request::CreateTransactionRequest;
use crate::client::create_transaction::create_transaction;

#[tokio::main]
async fn main() {
    let req = CreateTransactionRequest {
        from: "000004".into(),
        to: "000005".into(),
        amount: Decimal::new(100045, 02), // 1000.45
        comments: "Transferência para pagamento de serviços".into(),
    };

    create_transaction(req).await;
}
