pub mod blog;
pub mod comment;
pub mod goods;
pub mod user;

use once_cell::race::OnceBox;
use sea_orm::{ConnectOptions, Database, DatabaseConnection, DbErr};
use std::time::Duration;

static DB: OnceBox<DatabaseConnection> = OnceBox::new();

pub async fn init(data_source: &str) -> Result<(), DbErr> {
    let mut opt = ConnectOptions::new(data_source.to_string());
    opt.max_connections(100)
        .min_connections(5)
        .connect_timeout(Duration::from_secs(8))
        .idle_timeout(Duration::from_secs(8))
        .max_lifetime(Duration::from_secs(8))
        .sqlx_logging(false)
        .sqlx_logging_level(log::LevelFilter::Info);
    match Database::connect(opt).await {
        Ok(db) => {
            DB.set(Box::new(db)).unwrap();
            Ok(())
        }
        Err(e) => Err(e),
    }
}
