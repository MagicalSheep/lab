package cn.magicalsheep.wbclient.data

const val OK: Int = 200
const val ERROR: Int = 500

data class AjaxResult<T>(
    val code: Int,
    val msg: String,
    val data: T
)