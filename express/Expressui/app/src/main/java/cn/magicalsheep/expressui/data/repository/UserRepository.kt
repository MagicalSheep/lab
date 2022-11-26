package cn.magicalsheep.expressui.data.repository

import cn.magicalsheep.expressui.baseUrl
import cn.magicalsheep.expressui.data.entity.User
import cn.magicalsheep.expressui.data.entity.UserDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


var userRepositoryGson: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .create()

val userRepository: UserRepository = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(userRepositoryGson))
    .build()
    .create(UserRepository::class.java);

interface UserRepository {

    @POST("user/register")
    suspend fun register(@Body req: UserDto): cn.magicalsheep.expressui.data.entity.Result<User>

    @POST("user/login")
    suspend fun login(@Body req: UserDto): cn.magicalsheep.expressui.data.entity.Result<String>

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): cn.magicalsheep.expressui.data.entity.Result<List<User>>

    @PUT("user/permission")
    suspend fun addPermission(
        @Header("Authorization") token: String,
        @Query("account") account: String,
        @Query("package_id") packageId: Long
    ): cn.magicalsheep.expressui.data.entity.Result<Any>

    @DELETE("user/permission")
    suspend fun removePermission(
        @Header("Authorization") token: String,
        @Query("account") account: String,
        @Query("package_id") packageId: Long
    ): cn.magicalsheep.expressui.data.entity.Result<Any>
}