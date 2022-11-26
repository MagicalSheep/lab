package cn.magicalsheep.wbclient.data.repository

import cn.magicalsheep.wbclient.data.User

interface UserRepository {

    suspend fun getUserByToken(token: String): User
    suspend fun login(userName: String, pwd: String): String
    suspend fun register(userName: String, pwd: String)
    fun logout()

}