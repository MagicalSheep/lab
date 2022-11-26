use crate::entity::prelude::Comment;
use crate::entity::{comment, goods, user};
use crate::packets::CommentDetail;
use crate::repository::DB;
use sea_orm::*;

pub async fn save_comment(comment: comment::ActiveModel) -> Result<comment::ActiveModel, DbErr> {
    let db = DB.get().unwrap();
    let res: comment::ActiveModel = comment.save(db).await?;
    Ok(res)
}

pub async fn get_comments_by_blog_id(
    blog_id: i32,
    page: usize,
    page_num: usize,
) -> Result<Vec<CommentDetail>, DbErr> {
    let db = DB.get().unwrap();
    let pg = Comment::find()
        .filter(
            Condition::all()
                .add(comment::Column::BlogTarget.eq(blog_id))
                .add(comment::Column::CommentTarget.is_null()),
        )
        .order_by_desc(comment::Column::CreateTime)
        .column_as(comment::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(comment::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(comment::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, comment::Relation::User.def())
        .join(JoinType::LeftJoin, comment::Relation::Goods.def())
        .group_by(comment::Column::Id)
        .into_model::<CommentDetail>()
        .paginate(db, page_num)
        .fetch_page(page - 1)
        .await?;
    Ok(pg)
}

pub async fn get_comments_by_comment_id(
    comment_id: i32,
    page: usize,
    page_num: usize,
) -> Result<Vec<CommentDetail>, DbErr> {
    let db = DB.get().unwrap();
    let pg = Comment::find()
        .filter(
            Condition::all()
                .add(comment::Column::CommentTarget.is_not_null())
                .add(comment::Column::CommentTarget.eq(comment_id)),
        )
        .order_by_desc(comment::Column::CreateTime)
        .column_as(comment::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(comment::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(comment::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, comment::Relation::User.def())
        .join(JoinType::LeftJoin, comment::Relation::Goods.def())
        .group_by(comment::Column::Id)
        .into_model::<CommentDetail>()
        .paginate(db, page_num)
        .fetch_page(page - 1)
        .await?;
    Ok(pg)
}

pub async fn get_comment_by_id(id: i32) -> Result<Option<CommentDetail>, DbErr> {
    let db = DB.get().unwrap();
    let res = Comment::find()
        .filter(comment::Column::Id.eq(id))
        .column_as(comment::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(comment::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(comment::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, comment::Relation::User.def())
        .join(JoinType::LeftJoin, comment::Relation::Goods.def())
        .group_by(comment::Column::Id)
        .into_model::<CommentDetail>()
        .one(db)
        .await?;
    Ok(res)
}

pub async fn del_comment(comment: comment::ActiveModel) -> Result<DeleteResult, DbErr> {
    let db = DB.get().unwrap();
    Ok(comment.delete(db).await?)
}
