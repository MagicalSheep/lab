package cn.magicalsheep.wbclient.data.repository

import cn.magicalsheep.wbclient.data.Blog
import kotlinx.coroutines.flow.Flow

interface BlogRepository {

    suspend fun getBlogs(page: Int, pageNum: Int): Flow<List<Blog>>
    suspend fun getBlog(blogId: Int): Flow<Blog>
    suspend fun getBlogsByAuthor(author: String, page: Int, pageNum: Int): Flow<List<Blog>>
    suspend fun addBlog(content: String, reference: Int?)
    suspend fun addGood(blogId: Int, commentId: Int?)

}