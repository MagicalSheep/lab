package cn.magicalsheep.wbclient.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.Comment
import cn.magicalsheep.wbclient.data.repository.CommentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class CommentSource(private val blog: Blog?, private val commentRepository: CommentRepository) :
    PagingSource<Int, Comment>() {

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        try {
            val page: Int = params.key ?: 1
            if (blog == null) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                )
            }
            val response = commentRepository.getComments(blog.id, page, params.loadSize)
            return LoadResult.Page(
                data = response.first(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.first().isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}