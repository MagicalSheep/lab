package cn.magicalsheep.wbclient.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.repository.BlogRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class BlogSource(private val blogRepository: BlogRepository) : PagingSource<Int, Blog>() {

    override fun getRefreshKey(state: PagingState<Int, Blog>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Blog> {
        return try {
            val page: Int = params.key ?: 1
            val response = blogRepository.getBlogs(page, params.loadSize)
            LoadResult.Page(
                data = response.first(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.first().isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}