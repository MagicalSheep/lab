package cn.magicalsheep.expressui.ui.paging

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.magicalsheep.expressui.data.repository.packageRepository

class PackageSource(private val sharedPreferences: SharedPreferences) :
    PagingSource<Int, cn.magicalsheep.expressui.data.entity.Package>() {

    override fun getRefreshKey(state: PagingState<Int, cn.magicalsheep.expressui.data.entity.Package>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, cn.magicalsheep.expressui.data.entity.Package> {
        return try {
            val page: Int = params.key ?: 1
            val token = sharedPreferences.getString("token", "") ?: ""
            val response = packageRepository.getPackages(token, page, params.loadSize)
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