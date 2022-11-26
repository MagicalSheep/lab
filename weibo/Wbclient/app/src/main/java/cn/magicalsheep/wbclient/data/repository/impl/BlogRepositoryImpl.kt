package cn.magicalsheep.wbclient.data.repository.impl

import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.OK
import cn.magicalsheep.wbclient.data.remote.BlogInterface
import cn.magicalsheep.wbclient.data.remote.RetrofitBuilder
import cn.magicalsheep.wbclient.data.remote.Token
import cn.magicalsheep.wbclient.data.remote.model.BlogRequestBody
import cn.magicalsheep.wbclient.data.remote.model.GoodRequestBody
import cn.magicalsheep.wbclient.data.repository.BlogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BlogRepositoryImpl : BlogRepository {

    private val blogApi: BlogInterface = RetrofitBuilder.blogApi

    override suspend fun getBlogs(page: Int, pageNum: Int): Flow<List<Blog>> {
        val res = blogApi.getBlog(
            token = Token.token,
            id = null,
            author = null,
            page = page,
            pageNum = pageNum
        )
        if (res.code != OK) {
            throw Exception("获取微博错误：" + res.msg)
        }
        return flow { emit(res.data) }
    }

    override suspend fun getBlog(blogId: Int): Flow<Blog> {
        val res = blogApi.getBlog(token = Token.token, id = blogId, null, 1, 1)
        if (res.code != OK) {
            throw Exception("获取微博错误：" + res.msg)
        }
        if (res.data.isEmpty()) {
            throw Exception("服务器返回数据有误")
        }
        return flow { emit(res.data[0]) }
    }

    override suspend fun getBlogsByAuthor(
        author: String,
        page: Int,
        pageNum: Int
    ): Flow<List<Blog>> {
        val res =
            blogApi.getBlog(token = Token.token, id = null, author = author, page = page, pageNum = pageNum)
        if (res.code != OK) {
            throw Exception("获取微博错误：" + res.msg)
        }
        return flow { emit(res.data) }
    }

    override suspend fun addBlog(content: String, reference: Int?) {
        val res = blogApi.putBlog(token = Token.token, BlogRequestBody(content))
        if (res.code != OK) {
            throw Exception("发布微博失败：" + res.msg)
        }
    }

    override suspend fun addGood(blogId: Int, commentId: Int?) {
        val res = blogApi.putGood(token = Token.token, GoodRequestBody(blogId, commentId))
        if (res.code != OK) {
            throw Exception("点赞失败：" + res.msg)
        }
    }

}