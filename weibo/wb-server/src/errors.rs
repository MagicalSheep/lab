use sea_orm::DbErr;
use std::fmt::Debug;
use validator::{ValidationErrors, ValidationErrorsKind};

#[derive(Debug)]
pub struct CustomRejection {
    pub message: String,
}

impl warp::reject::Reject for CustomRejection {}

impl From<&str> for CustomRejection {
    fn from(msg: &str) -> Self {
        CustomRejection {
            message: msg.to_string(),
        }
    }
}

impl From<ValidationErrors> for CustomRejection {
    fn from(err: ValidationErrors) -> Self {
        let mut msg = "Request struct error";
        for err_kind in err.errors().values() {
            if let ValidationErrorsKind::Field(vec) = err_kind {
                msg = match vec.get(0) {
                    None => "Unknown validate error",
                    Some(e) => match &e.message {
                        None => "Unknown validate error",
                        Some(str) => str,
                    },
                }
            }
            break;
        }
        CustomRejection {
            message: msg.to_string(),
        }
    }
}

impl From<DbErr> for CustomRejection {
    fn from(err: DbErr) -> Self {
        CustomRejection {
            message: format!("{}", err),
        }
    }
}
