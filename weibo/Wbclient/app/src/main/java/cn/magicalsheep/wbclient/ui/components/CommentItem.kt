package cn.magicalsheep.wbclient.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.Comment
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CommentItem(
    blogId: Int,
    comment: Comment,
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {

    var isExpandReplyCard by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .clickable { isExpandReplyCard = !isExpandReplyCard },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    painter = if (comment.author == stringResource(id = R.string.sheep_name)) {
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
                        text = comment.author,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = comment.createTime,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                homeViewModel.addGood(blogId, comment.id)
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.wrapContentWidth(),
                                text = comment.goods.toString(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_thumb_up_off_alt_24),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "点赞"
                            )
                        }
                    }
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
//            AnimatedVisibility(visible = false) {
//                ReplyCard(targetId = comment.id, isReplyBlog = false)
//            }
        }
    }
}