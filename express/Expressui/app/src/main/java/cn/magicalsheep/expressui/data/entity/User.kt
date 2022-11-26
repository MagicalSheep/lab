package cn.magicalsheep.expressui.data.entity

import java.io.Serializable

data class User(
    val account: String,
    val role: String,
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val gender: String? = null
) : Serializable
