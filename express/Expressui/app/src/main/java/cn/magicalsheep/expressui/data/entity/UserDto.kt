package cn.magicalsheep.expressui.data.entity

import java.io.Serializable

data class UserDto(
    val account: String,
    val pwd: String ?= null,
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val gender: String? = null
) : Serializable
