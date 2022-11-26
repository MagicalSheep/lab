//! SeaORM Entity. Generated by sea-orm-codegen 0.9.2

use sea_orm::entity::prelude::*;

#[derive(Clone, Debug, PartialEq, DeriveEntityModel)]
#[sea_orm(table_name = "goods")]
pub struct Model {
    #[sea_orm(primary_key)]
    pub id: i32,
    pub user: i32,
    pub blog_id: i32,
    pub comment_id: Option<i32>,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {
    #[sea_orm(
        belongs_to = "super::blog::Entity",
        from = "Column::BlogId",
        to = "super::blog::Column::Id",
        on_update = "Cascade",
        on_delete = "Cascade"
    )]
    Blog,
    #[sea_orm(
        belongs_to = "super::comment::Entity",
        from = "Column::CommentId",
        to = "super::comment::Column::Id",
        on_update = "Cascade",
        on_delete = "Cascade"
    )]
    Comment,
    #[sea_orm(
        belongs_to = "super::user::Entity",
        from = "Column::User",
        to = "super::user::Column::Id",
        on_update = "Cascade",
        on_delete = "Cascade"
    )]
    User,
}

impl Related<super::blog::Entity> for Entity {
    fn to() -> RelationDef {
        Relation::Blog.def()
    }
}

impl Related<super::comment::Entity> for Entity {
    fn to() -> RelationDef {
        Relation::Comment.def()
    }
}

impl Related<super::user::Entity> for Entity {
    fn to() -> RelationDef {
        Relation::User.def()
    }
}

impl ActiveModelBehavior for ActiveModel {}