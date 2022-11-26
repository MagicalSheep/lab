package cn.magicalsheep.wbclient.data.remote

import cn.magicalsheep.wbclient.data.AjaxResult
import cn.magicalsheep.wbclient.data.User
import cn.magicalsheep.wbclient.data.remote.model.LoginBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserInterface {

    @POST("login")
    suspend fun login(@Body body: LoginBody): AjaxResult<String?>

    @POST("register")
    suspend fun register(@Body body: LoginBody): AjaxResult<Unit>

    @GET("profile")
    suspend fun profile(@Header("Authorization") token: String): AjaxResult<User>

}