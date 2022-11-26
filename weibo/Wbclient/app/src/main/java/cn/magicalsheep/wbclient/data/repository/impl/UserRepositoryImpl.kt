package cn.magicalsheep.wbclient.data.repository.impl

import cn.magicalsheep.wbclient.data.OK
import cn.magicalsheep.wbclient.data.User
import cn.magicalsheep.wbclient.data.remote.RetrofitBuilder
import cn.magicalsheep.wbclient.data.remote.Token
import cn.magicalsheep.wbclient.data.remote.UserInterface
import cn.magicalsheep.wbclient.data.remote.model.LoginBody
import cn.magicalsheep.wbclient.data.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    private val userApi: UserInterface = RetrofitBuilder.userApi

    override suspend fun getUserByToken(token: String): User {
        val res = userApi.profile(token)
        if (res.code != OK) {
            throw Exception("获取用户信息失败：" + res.msg)
        }
        Token.token = token
        return res.data
    }

    override suspend fun login(userName: String, pwd: String): String {
        val res = userApi.login(LoginBody(name = userName, pwd = pwd))
        if (res.code != OK) {
            throw Exception("登录失败：" + res.msg)
        }
        if (res.data == null) {
            throw Exception("登录失败：服务器没有返回正确的Token")
        }
        Token.token = res.data
        return res.data
    }

    override suspend fun register(userName: String, pwd: String) {
        val res = userApi.register(LoginBody(name = userName, pwd = pwd))
        if (res.code != OK) {
            throw Exception("注册失败：" + res.msg)
        }
    }

    override fun logout() {
        Token.token = ""
    }
}