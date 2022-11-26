use crate::entity::goods;
use crate::errors::CustomRejection;
use crate::handlers::token_check;
use crate::packets::{AjaxResult, GoodsPacket};
use crate::repository;
use warp::http::StatusCode;
use warp::{Rejection, Reply};

pub async fn add_good(packet: GoodsPacket, token: String) -> Result<impl Reply, Rejection> {
    let user = token_check(&token).await?;
    let op = repository::goods::save_good(goods::Model {
        id: 0,
        user: user.0,
        blog_id: packet.blog_id,
        comment_id: packet.comment_id,
    })
    .await;
    let ret: AjaxResult<()>;
    match op {
        Ok(_) => {
            ret = AjaxResult {
                code: StatusCode::OK.as_u16(),
                msg: "操作成功".to_string(),
                data: None,
            }
        }
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn del_good(packet: GoodsPacket, token: String) -> Result<impl Reply, Rejection> {
    let user = token_check(&token).await?;
    let res =
        repository::goods::find_by_good_index(user.0, packet.blog_id, packet.comment_id).await;
    let tmp;
    match res {
        Ok(r) => match r {
            None => return Err(warp::reject::custom(CustomRejection::from("该赞不存在"))),
            Some(gd) => tmp = repository::goods::del_good(gd).await,
        },
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    };
    let ret;
    match tmp {
        Ok(r) => {
            ret = AjaxResult {
                code: StatusCode::OK.as_u16(),
                msg: "取消成功".to_string(),
                data: Some(r.rows_affected),
            }
        }
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}
