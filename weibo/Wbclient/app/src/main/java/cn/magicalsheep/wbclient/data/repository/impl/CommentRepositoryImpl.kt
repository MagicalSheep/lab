package cn.magicalsheep.wbclient.data.repository.impl

import cn.magicalsheep.wbclient.data.Comment
import cn.magicalsheep.wbclient.data.OK
import cn.magicalsheep.wbclient.data.remote.CommentInterface
import cn.magicalsheep.wbclient.data.remote.RetrofitBuilder
import cn.magicalsheep.wbclient.data.remote.Token
import cn.magicalsheep.wbclient.data.remote.model.CommentRequestBody
import cn.magicalsheep.wbclient.data.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommentRepositoryImpl : CommentRepository {

    private val commentApi: CommentInterface = RetrofitBuilder.commentApi

    override suspend fun getComments(blogId: Int, page: Int, pageNum: Int): Flow<List<Comment>> {
        val res = commentApi.getComment(
            token = Token.token,
            commentTarget = null,
            blogTarget = blogId,
            page = page,
            pageNum = pageNum
        )
        if (res.code != OK) {
            throw Exception("获取评论失败：" + res.msg)
        }
        return flow { emit(res.data) }
    }

    override suspend fun addComment(content: String, blogId: Int) {
        val res = commentApi.putComment(
            token = Token.token,
            body = CommentRequestBody(blogId = blogId, content = content)
        )
        if (res.code != OK) {
            throw Exception("添加评论失败：" + res.msg)
        }
    }
}