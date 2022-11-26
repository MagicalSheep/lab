use crate::errors::CustomRejection;
use crate::handlers;
use crate::packets::{BlogsQuery, CommentsQuery, LoginPacket};
use std::collections::HashMap;
use validator::Validate;
use warp::{Filter, Rejection};

// PASS
/// POST /login { name: String, pwd: String }
pub fn login() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("login")
        .and(warp::post())
        .and(warp::body::json())
        .and_then(|packet: LoginPacket| async move {
            if let Err(e) = packet.validate() {
                Err(warp::reject::custom(CustomRejection::from(e)))
            } else {
                Ok(packet)
            }
        })
        .and_then(handlers::user::login)
}

// PASS
/// POST /register { name: String, pwd: String, sex: String(Optional),
/// city: String(Optional), moto: String(Optional) }
pub fn register() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("register")
        .and(warp::post())
        .and(warp::body::json())
        .and_then(|packet: LoginPacket| async move {
            if let Err(e) = packet.validate() {
                Err(warp::reject::custom(CustomRejection::from(e)))
            } else {
                Ok(packet)
            }
        })
        .and_then(handlers::user::register)
}

/// GET /profile
pub fn get_profile() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("profile")
        .and(warp::get())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::user::get_profile)
}

/// GET /blog { id: i32(Optional), author: String(Optional), page: i32, page_num: i32 }
pub fn get_blogs() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("blog")
        .and(warp::query::<BlogsQuery>())
        .and(warp::get())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::blog::get_blogs)
}

/// PUT /blog { content: String }
pub fn add_blog() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("blog")
        .and(warp::put())
        .and(warp::body::json())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::blog::add_blog)
}

/// DELETE /blog { id: i32 }
pub fn del_blog() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("blog")
        .and(warp::query::<HashMap<String, i32>>())
        .and(warp::delete())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::blog::del_blog)
}

/// PUT /good { blog_id: i32, comment_id: i32(Optional) }
pub fn add_good() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("good")
        .and(warp::put())
        .and(warp::body::json())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::goods::add_good)
}

/// DELETE /good { blog_id: i32, comment_id: i32(Optional) }
pub fn del_good() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("good")
        .and(warp::delete())
        .and(warp::body::json())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::goods::del_good)
}

/// GET /comment { comment_target: i32(Optional), blog_target: i32, page: usize, page_num: usize }
pub fn get_comments() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("comment")
        .and(warp::query::<CommentsQuery>())
        .and(warp::get())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::comment::get_comments)
}

/// PUT /comment { blog_id: i32, comment_id: i32(Optional), content: String }
pub fn add_comment() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("comment")
        .and(warp::put())
        .and(warp::body::json())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::comment::add_comment)
}

/// DELETE /comment { id: i32 }
pub fn del_comment() -> impl Filter<Extract = impl warp::Reply, Error = Rejection> + Clone {
    warp::path!("comment")
        .and(warp::query::<HashMap<String, i32>>())
        .and(warp::delete())
        .and(warp::header::<String>("Authorization"))
        .and_then(handlers::comment::del_comment)
}
