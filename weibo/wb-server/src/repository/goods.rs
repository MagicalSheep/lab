use crate::entity::goods;
use crate::entity::prelude::Goods;
use crate::repository::DB;
use sea_orm::*;

pub async fn find_by_good_index(
    user: i32,
    blog_id: i32,
    comment_id: Option<i32>,
) -> Result<Option<goods::Model>, DbErr> {
    let db = DB.get().unwrap();
    Ok(Goods::find()
        .filter(
            Condition::all()
                .add(goods::Column::User.eq(user))
                .add(goods::Column::BlogId.eq(blog_id))
                .add(if let Some(cid) = comment_id {
                    goods::Column::CommentId.eq(cid)
                } else {
                    goods::Column::CommentId.is_null()
                }),
        )
        .one(db)
        .await?)
}

pub async fn save_good(good: goods::Model) -> Result<goods::ActiveModel, DbErr> {
    let db = DB.get().unwrap();
    if let None = good.comment_id {
        if let Some(_) = Goods::find()
            .filter(
                Condition::all()
                    .add(goods::Column::User.eq(good.user))
                    .add(goods::Column::BlogId.eq(good.blog_id))
                    .add(goods::Column::CommentId.is_null()),
            )
            .one(db)
            .await?
        {
            return Err(DbErr::Custom("该微博已点赞".to_string()));
        }
    }
    let mut gd: goods::ActiveModel = good.into();
    gd.id = NotSet;
    let res: goods::ActiveModel = gd.save(db).await?;
    Ok(res)
}

pub async fn del_good(good: goods::Model) -> Result<DeleteResult, DbErr> {
    let db = DB.get().unwrap();
    Ok(good.delete(db).await?)
}
