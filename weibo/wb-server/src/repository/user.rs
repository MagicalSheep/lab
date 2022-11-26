use crate::entity::prelude::User;
use crate::entity::user;
use crate::repository::DB;
use sea_orm::*;

pub async fn find_by_id(id: i32) -> Result<Option<user::Model>, DbErr> {
    let db = DB.get().unwrap();
    User::find().filter(user::Column::Id.eq(id)).one(db).await
}

pub async fn find_by_name(name: &str) -> Result<Option<user::Model>, DbErr> {
    let db = DB.get().unwrap();
    User::find()
        .filter(user::Column::Name.eq(name))
        .one(db)
        .await
}

pub async fn save_user(user: user::ActiveModel) -> Result<user::ActiveModel, DbErr> {
    let db = DB.get().unwrap();
    let res: user::ActiveModel = user.save(db).await?;
    Ok(res)
}
