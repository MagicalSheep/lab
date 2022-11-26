extern crate core;

mod entity;
mod errors;
mod filters;
mod handlers;
mod packets;
mod repository;
mod util;

use serde_derive::{Deserialize, Serialize};
use std::env;
use std::io::Read;
use std::net::SocketAddr;
use warp::Filter;

#[derive(Debug, Serialize, Deserialize)]
struct Config {
    data_source: String,
    address: String,
    port: u16,
}

static JWT_SECRET: &str = "hello-world";

#[tokio::main]
async fn main() {
    if env::var_os("RUST_LOG").is_none() {
        env::set_var("RUST_LOG", "info");
    }
    pretty_env_logger::init();

    let mut file = std::fs::File::open("config.yaml").expect("Read config failed");
    let mut yaml_str = String::new();
    file.read_to_string(&mut yaml_str)
        .expect("Read config failed");
    let config: Config = serde_yaml::from_str(&yaml_str).expect("Read config failed");

    repository::init(&config.data_source)
        .await
        .expect("Initialize database failed");

    let routes = filters::login()
        .or(filters::register())
        .or(filters::get_profile())
        .or(filters::get_blogs())
        .or(filters::add_blog())
        .or(filters::del_blog())
        .or(filters::get_comments())
        .or(filters::add_comment())
        .or(filters::del_comment())
        .or(filters::add_good())
        .or(filters::del_good())
        .recover(handlers::err_handle);

    let addr = SocketAddr::new(
        config.address.parse().expect("Parse address failed"),
        config.port,
    );

    warp::serve(routes).run(addr).await;
}
