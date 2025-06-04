use bigdecimal::BigDecimal;
use sqlx::mysql::MySqlConnection;
use sqlx::{Connection, mysql};

#[derive(sqlx::FromRow)]
pub struct Account {
    pub nu_account: String,
    pub balance: BigDecimal,
}

async fn connect() -> MySqlConnection {
    let opt = mysql::MySqlConnectOptions::new()
        .host("localhost")
        .port(3306)
        .username("root")
        .password("123456")
        .database("voidbank");

    return mysql::MySqlConnection::connect_with(&opt).await.unwrap();
}

pub async fn get_accounts() -> Vec<Account> {
    let mut conn = connect().await;
    return sqlx::query_as("select * from accounts")
        .fetch_all(&mut conn)
        .await
        .unwrap();
}
