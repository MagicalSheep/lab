package cn.magicalsheep.expressui.data.entity

import java.io.Serializable

data class Result<T> (
    val status: Int,
    val message: String,
    val data: T? = null
) : Serializable
