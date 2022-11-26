use crate::errors::CustomRejection;
use crate::handlers::token_check;
use crate::packets::{AjaxResult, LoginPacket, UserDetail};
use crate::repository::user::{find_by_id, find_by_name, save_user};
use crate::util::{create_token, Claims};
use bcrypt::verify;
use chrono::{DateTime, Duration, Local};
use validator::Validate;
use warp::http::StatusCode;
use warp::{Rejection, Reply};

pub async fn login(packet: LoginPacket) -> Result<impl Reply, Rejection> {
    if let Err(e) = packet.validate() {
        return Err(warp::reject::custom(CustomRejection::from(e)));
    }
    let user;
    match find_by_name(&packet.name).await {
        Ok(res) => match res {
            None => return Err(warp::reject::custom(CustomRejection::from("用户名不存在"))),
            Some(res) => user = res,
        },
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    let res;
    match verify(&packet.pwd, &user.pwd) {
        Ok(c) => res = c,
        Err(_) => return Err(warp::reject::custom(CustomRejection::from("密码错误"))),
    }
    let ret;
    if res {
        let now: DateTime<Local> = Local::now();
        let exp: DateTime<Local> = now + Duration::days(7);
        ret = AjaxResult {
            code: StatusCode::OK.as_u16(),
            msg: "登录成功".to_string(),
            data: Some(create_token(Claims {
                sub: packet.name,
                exp: exp.timestamp_millis(),
                id: user.id,
            })),
        }
    } else {
        ret = AjaxResult {
            code: StatusCode::FORBIDDEN.as_u16(),
            msg: "密码错误".to_string(),
            data: None,
        }
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn register(packet: LoginPacket) -> Result<impl Reply, Rejection> {
    if let Err(e) = packet.validate() {
        return Err(warp::reject::custom(CustomRejection::from(e)));
    }
    let res;
    match find_by_name(&packet.name).await {
        Ok(c) => res = c,
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    if None != res {
        return Err(warp::reject::custom(CustomRejection::from("用户名已存在")));
    }
    match save_user(packet.into()).await {
        Ok(_) => {}
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    let ret: AjaxResult<()> = AjaxResult {
        code: StatusCode::OK.as_u16(),
        msg: "注册成功".to_string(),
        data: None,
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn get_profile(token: String) -> Result<impl Reply, Rejection> {
    let (id, _) = token_check(&token).await?;
    let user;
    match find_by_id(id).await {
        Ok(res) => match res {
            None => return Err(warp::reject::custom(CustomRejection::from("用户不存在"))),
            Some(res) => user = res,
        },
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    let ret = AjaxResult {
        code: StatusCode::OK.as_u16(),
        msg: "操作成功".to_string(),
        data: Some(UserDetail {
            id: user.id,
            name: user.name,
            sex: user.sex,
            city: user.city,
            moto: user.moto,
            avatar: user.avatar,
        }),
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}
