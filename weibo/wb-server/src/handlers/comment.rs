use crate::errors::CustomRejection;
use crate::handlers::token_check;
use crate::packets::{AjaxResult, CommentPacket, CommentsQuery};
use crate::{entity, repository};
use sea_orm::ActiveValue::Set;
use std::collections::HashMap;
use validator::Validate;
use warp::http::StatusCode;
use warp::{Rejection, Reply};

pub async fn get_comments(query: CommentsQuery, token: String) -> Result<impl Reply, Rejection> {
    token_check(&token).await?;
    let op = match query.comment_target {
        None => {
            repository::comment::get_comments_by_blog_id(
                query.blog_target,
                query.page,
                query.page_num,
            )
            .await
        }
        Some(cid) => {
            repository::comment::get_comments_by_comment_id(cid, query.page, query.page_num).await
        }
    };
    let ret;
    match op {
        Ok(res) => {
            ret = AjaxResult {
                code: StatusCode::OK.as_u16(),
                msg: "操作成功".to_string(),
                data: Some(res),
            }
        }
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn add_comment(packet: CommentPacket, token: String) -> Result<impl Reply, Rejection> {
    let user = token_check(&token).await?;
    if let Err(e) = packet.validate() {
        return Err(warp::reject::custom(CustomRejection::from(e)));
    }
    let mut comment: entity::comment::ActiveModel = packet.into();
    comment.author = Set(user.0);
    match repository::comment::save_comment(comment).await {
        Ok(_) => {}
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    let ret: AjaxResult<()> = AjaxResult {
        code: StatusCode::OK.as_u16(),
        msg: "发布评论成功".to_string(),
        data: None,
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn del_comment(
    params: HashMap<String, i32>,
    token: String,
) -> Result<impl Reply, Rejection> {
    let user = token_check(&token).await?;
    let id;
    match params.get("id") {
        None => {
            return Err(warp::reject::custom(CustomRejection::from(
                "评论ID参数不存在",
            )))
        }
        Some(x) => id = x.clone(),
    }
    let comment;
    match repository::comment::get_comment_by_id(id).await {
        Ok(res) => match res {
            None => return Err(warp::reject::custom(CustomRejection::from("评论不存在"))),
            Some(c) => comment = c,
        },
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    if !comment.author.eq(&user.1) {
        return Err(warp::reject::custom(CustomRejection::from("权限不足")));
    }
    let res = repository::comment::del_comment(entity::comment::ActiveModel {
        id: Set(comment.id),
        author: Set(user.0),
        content: Set((&comment.content).to_string()),
        blog_target: Set(0),
        comment_target: Set(None),
        create_time: Set(comment.create_time),
    });
    let ret;
    match res.await {
        Ok(r) => {
            ret = AjaxResult {
                code: StatusCode::OK.as_u16(),
                msg: "操作成功".to_string(),
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
