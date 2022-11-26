package cn.magicalsheep.wbclient.data

data class User(
    val id: Int,
    val name: String,
    val avatar: String? = null,
    val moto: String? = null,
    val sex: String? = null,
    val city: String? = null
)