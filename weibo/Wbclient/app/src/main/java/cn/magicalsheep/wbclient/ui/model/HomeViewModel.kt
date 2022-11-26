package cn.magicalsheep.wbclient.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.Comment
import cn.magicalsheep.wbclient.data.remote.BlogInterface
import cn.magicalsheep.wbclient.data.repository.BlogRepository
import cn.magicalsheep.wbclient.data.repository.CommentRepository
import cn.magicalsheep.wbclient.data.repository.impl.BlogRepositoryImpl
import cn.magicalsheep.wbclient.data.repository.impl.CommentRepositoryImpl
import cn.magicalsheep.wbclient.ui.paging.BlogSource
import cn.magicalsheep.wbclient.ui.paging.CommentSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val blogRepository: BlogRepository = BlogRepositoryImpl(),
    private val commentRepository: CommentRepository = CommentRepositoryImpl()
) :
    ViewModel() {

    private val _blogDetailState = MutableStateFlow(BlogDetailState())
    val blogDetailState: StateFlow<BlogDetailState> = _blogDetailState
    var commentList: Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20)
    ) {
        CommentSource(blogDetailState.value.blog, commentRepository)
    }.flow.cachedIn(viewModelScope);

    val blogList = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20)
    ) {
        BlogSource(blogRepository)
    }.flow.cachedIn(viewModelScope)

    fun setBlogDetailState() {
        commentList = Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20)
        ) {
            CommentSource(blogDetailState.value.blog, commentRepository)
        }.flow.cachedIn(viewModelScope)
    }

    suspend fun getBlogDetail(blogId: Int) {
        blogRepository.getBlog(blogId)
            .catch { ex ->
                _blogDetailState.value = BlogDetailState(error = ex.message)
            }
            .collect { blog ->
                _blogDetailState.value = BlogDetailState(blog = blog)
            }
    }

    suspend fun addBlog(content: String, reference: Int?) {
        blogRepository.addBlog(content, reference)
    }

    suspend fun addComment(content: String, blogId: Int) {
        commentRepository.addComment(content, blogId)
    }

    suspend fun addGood(blogId: Int, commentId: Int? = null) {
        blogRepository.addGood(blogId, commentId)
    }

}

data class BlogDetailState(
    val blog: Blog? = null,
    val error: String? = null,
    val comments: List<Comment> = emptyList()
)