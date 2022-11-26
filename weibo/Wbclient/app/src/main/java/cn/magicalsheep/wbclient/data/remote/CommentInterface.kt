package cn.magicalsheep.wbclient.data.remote

import cn.magicalsheep.wbclient.data.AjaxResult
import cn.magicalsheep.wbclient.data.Comment
import cn.magicalsheep.wbclient.data.remote.model.CommentRequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface CommentInterface {

    @GET("comment")
    suspend fun getComment(
        @Header("Authorization") token: String,
        @Query("comment_target") commentTarget: Int?,
        @Query("blog_target") blogTarget: Int,
        @Query("page") page: Int,
        @Query("page_num") pageNum: Int
    ): AjaxResult<List<Comment>>

    @PUT("comment")
    suspend fun putComment(
        @Header("Authorization") token: String,
        @Body body: CommentRequestBody
    ): AjaxResult<Unit>

    @DELETE("comment")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): AjaxResult<Int?>

}