use crate::entity::prelude::Blog;
use crate::entity::{blog, goods, user};
use crate::packets::BlogDetail;
use crate::repository::DB;
use sea_orm::*;

pub async fn save_blog(blog: blog::ActiveModel) -> Result<blog::ActiveModel, DbErr> {
    let db = DB.get().unwrap();
    let res: blog::ActiveModel = blog.save(db).await?;
    Ok(res)
}

pub async fn get_blogs(page: usize, page_num: usize) -> Result<Vec<BlogDetail>, DbErr> {
    let db = DB.get().unwrap();
    let pg = Blog::find()
        .filter(goods::Column::CommentId.is_null())
        .order_by_desc(blog::Column::CreateTime)
        .column_as(blog::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(blog::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(blog::Column::Reference, "reference")
        .column_as(blog::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, blog::Relation::User.def())
        .join(JoinType::LeftJoin, blog::Relation::Goods.def())
        .group_by(blog::Column::Id)
        .into_model::<BlogDetail>()
        .paginate(db, page_num)
        .fetch_page(page - 1)
        .await?;
    Ok(pg)
}

pub async fn get_blogs_by_author(
    author: &str,
    page: usize,
    page_num: usize,
) -> Result<Vec<BlogDetail>, DbErr> {
    let db = DB.get().unwrap();
    let pg = Blog::find()
        .filter(
            Condition::all()
                .add(user::Column::Name.eq(author))
                .add(goods::Column::CommentId.is_null()),
        )
        .order_by_desc(blog::Column::CreateTime)
        .column_as(blog::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(blog::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(blog::Column::Reference, "reference")
        .column_as(blog::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, blog::Relation::User.def())
        .join(JoinType::LeftJoin, blog::Relation::Goods.def())
        .group_by(blog::Column::Id)
        .into_model::<BlogDetail>()
        .paginate(db, page_num)
        .fetch_page(page - 1)
        .await?;
    Ok(pg)
}

pub async fn get_blog_by_id(id: i32) -> Result<Vec<BlogDetail>, DbErr> {
    let db = DB.get().unwrap();
    let res: Option<BlogDetail> = Blog::find()
        .filter(
            Condition::all()
                .add(blog::Column::Id.eq(id))
                .add(goods::Column::CommentId.is_null()),
        )
        .column_as(blog::Column::Id, "id")
        .column_as(user::Column::Name, "author")
        .column_as(blog::Column::Content, "content")
        .column_as(goods::Column::Id.count(), "goods")
        .column_as(blog::Column::Reference, "reference")
        .column_as(blog::Column::CreateTime, "create_time")
        .join(JoinType::InnerJoin, blog::Relation::User.def())
        .join(JoinType::LeftJoin, blog::Relation::Goods.def())
        .group_by(blog::Column::Id)
        .into_model::<BlogDetail>()
        .one(db)
        .await?;
    let vec = match res {
        Some(b) => vec![b],
        _ => vec![],
    };
    Ok(vec)
}

pub async fn del_blog(blog: blog::ActiveModel) -> Result<DeleteResult, DbErr> {
    let db = DB.get().unwrap();
    Ok(blog.delete(db).await?)
}
