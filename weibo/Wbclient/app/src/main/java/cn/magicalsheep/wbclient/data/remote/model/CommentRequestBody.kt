package cn.magicalsheep.wbclient.data.remote.model

import com.google.gson.annotations.SerializedName

data class CommentRequestBody(
    @SerializedName("blog_id")
    val blogId: Int,
    @SerializedName("comment_id")
    val commentId: Int? = null,
    val content: String
)