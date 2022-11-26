package cn.magicalsheep.wbclient.data.remote

import cn.magicalsheep.wbclient.data.AjaxResult
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.remote.model.BlogRequestBody
import cn.magicalsheep.wbclient.data.remote.model.GoodRequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface BlogInterface {

    @GET("blog")
    suspend fun getBlog(
        @Header("Authorization") token: String,
        @Query("id") id: Int?,
        @Query("author") author: String?,
        @Query("page") page: Int,
        @Query("page_num") pageNum: Int
    ): AjaxResult<List<Blog>>

    @PUT("blog")
    suspend fun putBlog(
        @Header("Authorization") token: String,
        @Body body: BlogRequestBody
    ): AjaxResult<Unit>

    @DELETE("blog")
    suspend fun deleteBlog(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): AjaxResult<Int?>

    @PUT("good")
    suspend fun putGood(
        @Header("Authorization") token: String,
        @Body body: GoodRequestBody
    ): AjaxResult<Unit>

    @DELETE("good")
    suspend fun deleteGood(
        @Header("Authorization") token: String,
        @Body body: GoodRequestBody
    ): AjaxResult<Int?>

}