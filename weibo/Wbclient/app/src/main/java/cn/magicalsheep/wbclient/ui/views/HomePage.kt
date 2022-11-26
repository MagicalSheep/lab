package cn.magicalsheep.wbclient.ui.views

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.ui.components.BlogItem
import cn.magicalsheep.wbclient.ui.components.EmptyPage
import cn.magicalsheep.wbclient.ui.components.SwipeRefreshList
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    onNavigationToDetail: (Int) -> Unit,
    onNavigationToEditBlog: () -> Unit,
    onPageDownScroll: () -> Unit,
    onPageUpScroll: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val blogLazyPagingItems = homeViewModel.blogList.collectAsLazyPagingItems()
//    val isLogin = profileViewModel.isLogin.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
//        val errorGetBlogs = stringResource(id = R.string.error_get_blogs)
        SwipeRefreshList(
            lazyPagingItems = blogLazyPagingItems,
            handleUpScroll = onPageUpScroll,
            handleDownScroll = onPageDownScroll,
//            handleError = {
//                if (isLogin.value) {
//                    scope.launch {
//                        snackbarHostState.showSnackbar(
//                            errorGetBlogs,
//                            withDismissAction = true
//                        )
//                    }
//                }
//            }
        ) {
            items(blogLazyPagingItems) { blog ->
                blog?.let {
                    BlogItem(
                        blog = blog,
                        onClick = { onNavigationToDetail(blog.id) },
                        snackbarHostState = snackbarHostState,
                        scope = scope,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = blogLazyPagingItems.loadState.refresh is LoadState.Error ||
                    blogLazyPagingItems.loadState.append is LoadState.Error ||
                    (blogLazyPagingItems.loadState.append.endOfPaginationReached && blogLazyPagingItems.itemCount == 0),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyPage()
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp, 16.dp),
                onClick = { onNavigationToEditBlog() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.add_blog)
                )
            }
        }

    }

}