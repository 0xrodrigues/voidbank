mod request;
mod client;
mod account;

use bigdecimal::BigDecimal;
use rand::rngs::ThreadRng;
use rand::Rng;
use rust_decimal::Decimal;
use rust_decimal::prelude::ToPrimitive;
use crate::request::create_transaction_request::CreateTransactionRequest;
use crate::client::create_transaction::create_transaction;
use crate::account::account::{get_accounts, Account};
use std::env;
use std::time::{Duration, Instant};
use tokio::time::sleep;

#[tokio::main]
async fn main() {
    let args: Vec<String> = env::args().collect();
    let command = args.get(1).map(|s| s.as_str()).unwrap_or("help");

    match command {
        "transactions" => {
            let duration_ms = args.get(2).and_then(|s| s.parse::<u64>().ok()).unwrap_or(10000);

            let accounts = get_accounts().await;
            if accounts.len() < 2 {
                println!("❌ Não há contas suficientes para fazer a transferência.");
                return;
            }

            run_transactions(accounts, duration_ms).await;
        },
        _ => {
            println!("Commands:");
            println!("transactions <tempo_em_ms>   - Executa transferências por esse tempo");
        }
    }
}

async fn run_transactions(accounts: Vec<Account>, duration_ms: u64) {
    let start = Instant::now();
    let mut rng = rand::thread_rng();

    while start.elapsed().as_millis() < duration_ms as u128 {
        let from_index = rng.gen_range(0..accounts.len());
        let mut to_index = rng.gen_range(0..accounts.len());

        while to_index == from_index {
            to_index = rng.gen_range(0..accounts.len());
        }

        let from_account = &accounts[from_index];
        let to_account = &accounts[to_index];

        let invalid = rng.gen_bool(0.2);
        let amount = generate_random_values(from_account.balance.clone(), &mut rng, invalid);

        let req = create_request(from_account.nu_account.clone(), to_account.nu_account.clone(), amount);

        println!(
            "Enviando R$ {} de {} para {} (forçando erro: {})",
            req.amount, req.from, req.to, invalid
        );

        create_transaction(req).await;

        // Pequeno intervalo para não bater a API a cada milissegundo
        sleep(Duration::from_millis(500)).await;
    }

    println!("✅ Bot finalizado após {} ms", duration_ms);
}

fn create_request(from_account: String, to_account: String, amount: Decimal) -> CreateTransactionRequest {
    CreateTransactionRequest {
        from: from_account,
        to: to_account,
        amount,
        comments: "Transaction Bot run".into(),
    }
}

fn generate_random_values(balance: BigDecimal, rng: &mut ThreadRng, invalid: bool) -> Decimal {
    let balance_f64 = balance.to_f64().unwrap_or(0.0);

    if invalid || balance_f64 < 0.01 {
        Decimal::new((balance_f64 * 100.0).ceil() as i64 + 10_000, 2)
    } else {
        let cents = (balance_f64 * 100.0).floor() as i64;
        let random_cents = rng.gen_range(1..=cents.max(1));
        Decimal::new(random_cents, 2)
    }
}