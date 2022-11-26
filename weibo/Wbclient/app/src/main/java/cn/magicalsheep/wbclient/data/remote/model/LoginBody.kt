package cn.magicalsheep.wbclient.data.remote.model

data class LoginBody(
    val name: String,
    val pwd: String,
    val sex: String? = null,
    val city: String? = null,
    val moto: String? = null
)