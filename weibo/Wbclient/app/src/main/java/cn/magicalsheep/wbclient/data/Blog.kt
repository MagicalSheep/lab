package cn.magicalsheep.wbclient.data

import com.google.gson.annotations.SerializedName

data class Blog(
    val id: Int,
    val author: String,
    val content: String,
    val goods: Int,
    val reference: Int?,
    @SerializedName("create_time")
    val createTime: String
)