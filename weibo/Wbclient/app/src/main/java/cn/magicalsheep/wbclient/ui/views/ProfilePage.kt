package cn.magicalsheep.wbclient.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.ui.components.BlogItem
import cn.magicalsheep.wbclient.ui.components.EmptyPage
import cn.magicalsheep.wbclient.ui.components.ProfileCard
import cn.magicalsheep.wbclient.ui.components.SwipeRefreshList
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    profileViewModel: ProfileViewModel,
    onNavigationToDetail: (Int) -> Unit,
    onNavigationToEditBlog: () -> Unit,
    onNavigationToHome: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onPageUpScroll: () -> Unit,
    onPageDownScroll: () -> Unit,
    homeViewModel: HomeViewModel
) {

    val loginUserState by profileViewModel.loginUserState.collectAsState()

    if (loginUserState.user == null || loginUserState.blogList == null) return EmptyPage()

    val blogLazyPagingItems = loginUserState.blogList!!.collectAsLazyPagingItems()
    var isCardVisiable by remember { mutableStateOf(true) }

    val logoutSuccess = stringResource(id = R.string.logout_success)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            AnimatedVisibility(visible = isCardVisiable) {
                loginUserState.user!!.let {
                    ProfileCard(user = it) {
                        scope.launch {
                            profileViewModel.logout()
                            snackbarHostState.showSnackbar(
                                logoutSuccess,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }
                        onNavigationToHome()
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                val errorGetBlogs = stringResource(id = R.string.error_get_blogs)
                SwipeRefreshList(
                    lazyPagingItems = blogLazyPagingItems,
                    handleDownScroll = {
                        onPageDownScroll()
                        isCardVisiable = false
                    },
                    handleUpScroll = {
                        onPageUpScroll()
                        isCardVisiable = true
                    },
                    handleError = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                errorGetBlogs,
                                withDismissAction = true
                            )
                        }
                    }
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
                androidx.compose.animation.AnimatedVisibility(
                    visible = blogLazyPagingItems.loadState.refresh is LoadState.Error ||
                            blogLazyPagingItems.loadState.append is LoadState.Error ||
                            (blogLazyPagingItems.loadState.append.endOfPaginationReached && blogLazyPagingItems.itemCount == 0),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    EmptyPage()
                }
            }
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