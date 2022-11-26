package cn.magicalsheep.wbclient.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun <T : Any> SwipeRefreshList(
    lazyPagingItems: LazyPagingItems<T>,
    handleDownScroll: () -> Unit = {},
    handleUpScroll: () -> Unit = {},
    handleError: () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()
    var lock by remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState(
            lazyPagingItems.loadState.refresh is LoadState.Loading
                    && lazyPagingItems.itemCount >= 0
        ),
        onRefresh = {
            lock = false
            lazyPagingItems.refresh()
        }
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            content()
            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                item { ListLoadingIndicator() }
            }
        }

        if (!lock && (lazyPagingItems.loadState.refresh is LoadState.Error ||
                    lazyPagingItems.loadState.append is LoadState.Error)
        ) {
            handleError()
            lock = true
        }

        if (lazyListState.isScrollingDown()) {
            handleDownScroll()
        } else if (lazyListState.isScrollingUp()) {
            handleUpScroll()
        }

    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
private fun LazyListState.isScrollingDown(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex < firstVisibleItemIndex
            } else {
                previousScrollOffset < firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}