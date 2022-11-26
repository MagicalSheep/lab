use crate::errors::CustomRejection;
use crate::handlers::token_check;
use crate::packets::{AjaxResult, BlogDetail, BlogPacket, BlogsQuery};
use crate::{entity, repository};
use sea_orm::ActiveValue::Set;
use std::collections::HashMap;
use validator::Validate;
use warp::http::StatusCode;
use warp::{Rejection, Reply};

pub async fn get_blogs(query: BlogsQuery, token: String) -> Result<impl Reply, Rejection> {
    token_check(&token).await?;
    let op = if let Some(id) = query.id {
        repository::blog::get_blog_by_id(id).await
    } else if let Some(name) = query.author {
        repository::blog::get_blogs_by_author(&name, query.page, query.page_num).await
    } else {
        repository::blog::get_blogs(query.page, query.page_num).await
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
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn add_blog(packet: BlogPacket, token: String) -> Result<impl Reply, Rejection> {
    if let Err(e) = packet.validate() {
        return Err(warp::reject::custom(CustomRejection::from(e)));
    }
    let user = token_check(&token).await?;
    let mut blog: entity::blog::ActiveModel = packet.into();
    blog.author = Set(user.0);
    match repository::blog::save_blog(blog).await {
        Ok(_) => {}
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    let ret: AjaxResult<()> = AjaxResult {
        code: StatusCode::OK.as_u16(),
        msg: "发布微博成功".to_string(),
        data: None,
    };
    Ok(warp::reply::with_status(
        warp::reply::json(&ret),
        StatusCode::OK,
    ))
}

pub async fn del_blog(
    params: HashMap<String, i32>,
    token: String,
) -> Result<impl Reply, Rejection> {
    let user = token_check(&token).await?;
    let id;
    match params.get("id") {
        None => {
            return Err(warp::reject::custom(CustomRejection::from(
                "微博ID参数不存在",
            )))
        }
        Some(x) => id = x.clone(),
    }
    let blog;
    match repository::blog::get_blog_by_id(id).await {
        Ok(res) => {
            if res.is_empty() {
                return Err(warp::reject::custom(CustomRejection::from("微博不存在")));
            } else {
                let b = res.get(0).unwrap();
                blog = BlogDetail {
                    id: b.id,
                    author: b.author.to_string(),
                    content: b.content.to_string(),
                    goods: b.goods,
                    reference: b.reference,
                    create_time: b.create_time,
                }
            }
        }
        Err(err) => return Err(warp::reject::custom(CustomRejection::from(err))),
    }
    if !blog.author.eq(&user.1) {
        return Err(warp::reject::custom(CustomRejection::from("权限不足")));
    }
    let res = repository::blog::del_blog(entity::blog::ActiveModel {
        id: Set(blog.id),
        author: Set(user.0),
        content: Set((&blog.content).to_string()),
        reference: Set(blog.reference),
        create_time: Set(blog.create_time),
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
