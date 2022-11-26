package cn.magicalsheep.wbclient.data.repository

import cn.magicalsheep.wbclient.data.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getComments(blogId: Int, page: Int, pageNum: Int): Flow<List<Comment>>
    suspend fun addComment(content: String, blogId: Int)

}