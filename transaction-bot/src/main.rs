mod account;
mod client;
mod request;

use crate::account::account::{Account, get_accounts};
use crate::client::request_client::RequestClient;
use crate::request::create_transaction_request::CreateTransactionRequest;
use bigdecimal::BigDecimal;
use log::LevelFilter;
use log::{info, warn};
use rand::Rng;
use rand::rngs::ThreadRng;
use rust_decimal::Decimal;
use rust_decimal::prelude::ToPrimitive;
use simple_logger::SimpleLogger;
use std::env;
use std::time::{Duration, Instant};
use tokio::time::sleep;

const ENDPOINT: &str = "http://localhost:8080/api/transaction";

#[tokio::main]
async fn main() {
    SimpleLogger::new()
        .env()
        .with_level(LevelFilter::Warn)
        .with_level(LevelFilter::Info)
        .init()
        .unwrap();

    let args: Vec<String> = env::args().collect();
    let command = args.get(1).map(|s| s.as_str()).unwrap_or("help");

    match command {
        "transactions" => {
            let duration_ms = args
                .get(2)
                .and_then(|s| s.parse::<u64>().ok())
                .unwrap_or(10000);

            let accounts = get_accounts().await;
            if accounts.len() < 2 {
                warn!("❌ Não há contas suficientes para fazer a transferência.");
                return;
            }

            run_transactions(accounts, duration_ms).await;
        }
        _ => {
            info!("Commands:");
            info!("transactions <tempo_em_ms>   - Executa transferências pelo tempo informado");
        }
    }
}

async fn run_transactions(accounts: Vec<Account>, duration_ms: u64) {
    let start = Instant::now();
    let mut rng = rand::thread_rng();

    while start.elapsed().as_millis() < duration_ms as u128 {
        let index = rand::seq::index::sample(&mut rng, accounts.len(), 2);
        let from_account = &accounts[index.index(0)];
        let to_account = &accounts[index.index(1)];

        let invalid = rng.gen_bool(0.2);
        let amount = generate_random_values(&from_account.balance, &mut rng, invalid);

        let req = create_request(
            from_account.nu_account.clone(),
            to_account.nu_account.clone(),
            amount,
        );

        info!(
            "Enviando R$ {} de {} para {} (forçando erro: {})",
            req.amount, req.from, req.to, invalid
        );

        // Send a request to API voidbank using struct and impl
        // Create e new Transaction to VOIDBANK API
        RequestClient::new(String::from(ENDPOINT))
            .send_create_transaction_request(req)
            .await;

        let delay = rng.gen_range(100..=300);
        sleep(Duration::from_millis(delay)).await;
    }

    info!("✅ Bot finalizado após {} ms", duration_ms);
}

fn create_request(from_account: i32, to_account: i32, amount: Decimal) -> CreateTransactionRequest {
    CreateTransactionRequest {
        from: from_account,
        to: to_account,
        transaction_type: String::from("PIX"),
        amount,
        comments: "Transaction Bot run".into(),
    }
}

fn generate_random_values(balance: &BigDecimal, rng: &mut ThreadRng, invalid: bool) -> Decimal {
    let balance_f64 = balance.to_f64().unwrap_or(0.0);

    if invalid || balance_f64 < 0.01 {
        Decimal::new((balance_f64 * 100.0).ceil() as i64 + 10_000, 2)
    } else {
        let cents = (balance_f64 * 100.0).floor() as i64;
        let random_cents = rng.gen_range(1..=cents.max(1));
        Decimal::new(random_cents, 2)
    }
}
