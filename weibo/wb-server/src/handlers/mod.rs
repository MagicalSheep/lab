pub mod blog;
pub mod comment;
pub mod goods;
pub mod user;

use crate::errors::CustomRejection;
use crate::packets::AjaxResult;
use crate::util::check_token;
use std::convert::Infallible;
use std::error::Error;
use warp::http::StatusCode;
use warp::{Rejection, Reply};

async fn token_check(token: &str) -> Result<(i32, String), Rejection> {
    match check_token(token.to_string()) {
        Ok(data) => Ok((data.claims.id, data.claims.sub)),
        Err(_) => Err(warp::reject::custom(CustomRejection::from(
            "Token无效或已过期，请重新登录",
        ))),
    }
}

pub async fn err_handle(err: Rejection) -> Result<impl Reply, Infallible> {
    let msg;

    if err.is_not_found() {
        msg = "404 Not Found".to_string();
    } else if let Some(e) = err.find::<warp::filters::body::BodyDeserializeError>() {
        msg = match e.source() {
            Some(cause) => cause.to_string(),
            None => "Unknown error".to_string(),
        }
    } else if let Some(e) = err.find::<CustomRejection>() {
        msg = (&e.message).to_string();
    } else {
        msg = "Interval error".to_string();
    }

    log::debug!("{}", msg);

    let json = warp::reply::json(&AjaxResult::<()> {
        code: 500,
        msg,
        data: None,
    });
    Ok(warp::reply::with_status(json, StatusCode::OK))
}
