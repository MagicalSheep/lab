use crate::entity;
use bcrypt::hash;
use chrono::{Local, NaiveDateTime};
use sea_orm::{ActiveValue, FromQueryResult};
use serde_derive::{Deserialize, Serialize};
use std::fmt::Debug;
use validator::Validate;

#[derive(Serialize)]
pub struct AjaxResult<T> {
    pub code: u16,
    pub msg: String,
    #[serde(skip_serializing_if = "Option::is_none")]
    pub data: Option<T>,
}

#[derive(Debug, Validate, Deserialize)]
pub struct LoginPacket {
    #[validate(length(min = 1, message = "用户名不能为空"))]
    #[validate(length(max = 20, message = "用户名过长"))]
    pub name: String,
    #[validate(length(min = 1, message = "密码不能为空"))]
    pub pwd: String,
    pub sex: Option<String>,
    pub city: Option<String>,
    pub moto: Option<String>,
    #[validate(url(message = "头像必须是一个图片地址"))]
    pub avatar: Option<String>,
}

#[derive(Debug, Validate, Deserialize)]
pub struct BlogPacket {
    #[validate(length(min = 1, max = 500, message = "微博长度不符合要求"))]
    pub content: String,
    pub reference: Option<i32>,
}

#[derive(Debug, Validate, Deserialize)]
pub struct CommentPacket {
    pub blog_id: i32,
    pub comment_id: Option<i32>,
    #[validate(length(max = 500, message = "Blog is too long"))]
    pub content: String,
}

#[derive(Debug, Validate, Deserialize)]
pub struct GoodsPacket {
    pub blog_id: i32,
    pub comment_id: Option<i32>,
}

#[derive(Deserialize, Serialize)]
pub struct BlogsQuery {
    pub id: Option<i32>,
    pub author: Option<String>,
    pub page: usize,
    pub page_num: usize,
}

#[derive(Deserialize, Serialize)]
pub struct CommentsQuery {
    pub comment_target: Option<i32>,
    pub blog_target: i32,
    pub page: usize,
    pub page_num: usize,
}

#[derive(FromQueryResult, Serialize)]
pub struct BlogDetail {
    pub id: i32,
    pub author: String,
    pub content: String,
    pub goods: i32,
    pub reference: Option<i32>,
    pub create_time: NaiveDateTime,
}

#[derive(FromQueryResult, Serialize)]
pub struct CommentDetail {
    pub id: i32,
    pub author: String,
    pub content: String,
    pub goods: i32,
    pub create_time: NaiveDateTime,
}

#[derive(Serialize)]
pub struct UserDetail {
    pub id: i32,
    pub name: String,
    pub sex: Option<String>,
    pub city: Option<String>,
    pub moto: Option<String>,
    pub avatar: Option<String>,
}

impl Into<entity::user::ActiveModel> for LoginPacket {
    fn into(self) -> entity::user::ActiveModel {
        let pwd;
        match hash(self.pwd, 4) {
            Ok(c) => pwd = c,
            Err(_) => panic!("!!Bcrypt Error!!"),
        }
        entity::user::ActiveModel {
            id: ActiveValue::NotSet,
            name: ActiveValue::Set(self.name),
            pwd: ActiveValue::Set(pwd),
            sex: ActiveValue::Set(self.sex),
            city: ActiveValue::Set(self.city),
            moto: ActiveValue::Set(self.moto),
            avatar: ActiveValue::Set(self.avatar),
        }
    }
}

impl Into<entity::blog::ActiveModel> for BlogPacket {
    fn into(self) -> entity::blog::ActiveModel {
        entity::blog::ActiveModel {
            id: ActiveValue::NotSet,
            author: ActiveValue::NotSet,
            content: ActiveValue::Set(self.content),
            reference: ActiveValue::NotSet,
            create_time: ActiveValue::Set(Local::now().naive_local()),
        }
    }
}

impl Into<entity::comment::ActiveModel> for CommentPacket {
    fn into(self) -> entity::comment::ActiveModel {
        entity::comment::ActiveModel {
            id: ActiveValue::NotSet,
            author: ActiveValue::NotSet,
            blog_target: ActiveValue::Set(self.blog_id),
            comment_target: ActiveValue::Set(self.comment_id),
            content: ActiveValue::Set(self.content),
            create_time: ActiveValue::Set(Local::now().naive_local()),
        }
    }
}
