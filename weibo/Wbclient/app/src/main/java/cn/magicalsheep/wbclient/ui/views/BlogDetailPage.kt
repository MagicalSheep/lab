package cn.magicalsheep.wbclient.ui.views

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.wbclient.ui.components.BlogItem
import cn.magicalsheep.wbclient.ui.components.CommentItem
import cn.magicalsheep.wbclient.ui.components.ReplyCard
import cn.magicalsheep.wbclient.ui.components.SwipeRefreshList
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun BlogDetailPage(
    blogId: Int,
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val blogDetailState by homeViewModel.blogDetailState.collectAsState()
    val commentLazyPagingItems = homeViewModel.commentList.collectAsLazyPagingItems()

    LaunchedEffect(
        key1 = "getBlogDetail",
        block = { homeViewModel.getBlogDetail(blogId) }
    )
    SwipeRefreshList(lazyPagingItems = commentLazyPagingItems, content = {
        item {
            blogDetailState.blog?.let {
                BlogItem(
                    blog = it,
                    onClick = {},
                    expandReplyCard = true,
                    expandContent = true,
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                    homeViewModel = homeViewModel
                )
            }
        }
        items(commentLazyPagingItems) { comment ->
            comment?.let {
                CommentItem(
                    blogId = blogId,
                    comment = comment,
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                    homeViewModel = homeViewModel
                )
            }
        }
    })
}