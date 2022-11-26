package cn.magicalsheep.wbclient.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

//    private const val BASE_URL = "https://mock.apifox.cn/m1/1580031-0-default/"
    private const val BASE_URL = "http://192.168.3.6:8080/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val blogApi: BlogInterface = getRetrofit().create(BlogInterface::class.java)
    val commentApi: CommentInterface = getRetrofit().create(CommentInterface::class.java)
    val userApi: UserInterface = getRetrofit().create(UserInterface::class.java)

}