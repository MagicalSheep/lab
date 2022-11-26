package cn.magicalsheep.wbclient.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogItem(
    blog: Blog,
    onClick: () -> Unit,
    expandReplyCard: Boolean = false,
    expandContent: Boolean = false,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    homeViewModel: HomeViewModel
) {

    var isExpandReplyCard by remember { mutableStateOf(expandReplyCard) }
    val developNote = stringResource(id = R.string.develop_note)
    val repostSuccess = stringResource(id = R.string.repost_success)
    val repost = stringResource(id = R.string.repost)

    Card(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 5.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    painter = if (blog.author == stringResource(id = R.string.sheep_name)) {
                        painterResource(id = R.drawable.sheep_avatar)
                    } else {
                        painterResource(id = R.drawable.baseline_account_circle_24)
                    }, size = 40.dp, shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = blog.author,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = blog.createTime,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                text = blog.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = if (expandContent) {
                    Int.MAX_VALUE
                } else {
                    5
                },
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier.wrapContentWidth(), onClick = {
                    scope.launch {
                        try {
                            homeViewModel.addGood(blog.id)
                        } catch (ex: Exception) {
                            ex.message?.let {
                                snackbarHostState.showSnackbar(
                                    it,
                                    withDismissAction = true
                                )
                            }
                        }
                    }
                }) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_thumb_up_off_alt_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(id = R.string.add_good)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = blog.goods.toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
                IconButton(onClick = { isExpandReplyCard = !isExpandReplyCard }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_comment_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = R.string.comment)
                    )
                }
                IconButton(onClick = {
                    scope.launch {
                        try {
                            homeViewModel.addBlog(repost, blog.id)
                            snackbarHostState.showSnackbar(
                                repostSuccess,
                                withDismissAction = true
                            )
                        } catch (ex: Exception) {
                            ex.message?.let {
                                snackbarHostState.showSnackbar(
                                    it,
                                    withDismissAction = true
                                )
                            }
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = R.string.repost)
                    )
                }
                IconButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            developNote,
                            withDismissAction = true
                        )
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = R.string.share)
                    )
                }
            }
            AnimatedVisibility(visible = isExpandReplyCard) {
                ReplyCard(
                    targetId = blog.id,
                    isReplyBlog = true,
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}