package cn.magicalsheep.expressui.ui.paging

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.magicalsheep.expressui.data.entity.User
import cn.magicalsheep.expressui.data.repository.packageRepository
import cn.magicalsheep.expressui.data.repository.userRepository

class UserSource(private val sharedPreferences: SharedPreferences) :
    PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page: Int = params.key ?: 1
            val token = sharedPreferences.getString("token", "") ?: ""
            val response = userRepository.getUsers(token, page, params.loadSize)
            val data = response.data ?: throw Exception("Data cannot be null")
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}