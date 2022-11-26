package cn.magicalsheep.expressserver.entity

import java.io.Serializable

data class Result (
    val status: Int,
    val message: String,
    val data: Any? = null
) : Serializable
