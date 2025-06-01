mod request;
mod client;
mod account;

use rand::Rng;
use rust_decimal::Decimal;
use rust_decimal::prelude::ToPrimitive;
use crate::request::create_transaction_request::CreateTransactionRequest;
use crate::client::create_transaction::create_transaction;
use crate::account::account::{get_accounts};

#[tokio::main]
async fn main() {
    let accounts = get_accounts().await;

    if accounts.len() < 2 {
        println!("Não há contas suficientes para fazer a transferência.");
        return;
    }

    while true {
        let mut rng = rand::thread_rng();
    let from_index = rng.gen_range(0..accounts.len());
    let mut to_index = rng.gen_range(0..accounts.len());

    while to_index == from_index {
        to_index = rng.gen_range(0..accounts.len());
    }

    let from_account = &accounts[from_index];
    let to_account = &accounts[to_index];

    // Convert BigDecimal -> f64 -> Decimal (com cuidado)
    let balance_f64 = from_account.balance.to_f64().unwrap_or(0.0);

    // 80% de chance de amount válido, 20% de chance de forçar erro
    let invalid = rng.gen_bool(0.2);
    let amount = if invalid || balance_f64 < 0.01 {
        Decimal::new((balance_f64 * 100.0).ceil() as i64 + 10_000, 2)
    } else {
        let cents = (balance_f64 * 100.0).floor() as i64;
        let random_cents = rng.gen_range(1..=cents.max(1));
        Decimal::new(random_cents, 2)
    };

    let req = CreateTransactionRequest {
        from: from_account.nu_account.clone(),
        to: to_account.nu_account.clone(),
        amount,
        comments: "Transaction Bot run".into(),
    };

    println!(
        "Enviando R$ {} de {} para {} (forçando erro: {})",
        req.amount, req.from, req.to, invalid
    );

    create_transaction(req).await;
    }
}