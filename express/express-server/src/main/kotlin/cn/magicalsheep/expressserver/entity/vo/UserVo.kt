package cn.magicalsheep.expressserver.entity.vo

import cn.magicalsheep.expressserver.entity.pojo.User
import java.io.Serializable

data class UserVo(
    val account: String,
    val role: String,
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val gender: String? = null
) : Serializable {
    constructor(user: User) : this(
        user.account,
        user.role,
        user.name,
        user.address,
        user.phone,
        user.gender
    )
}
