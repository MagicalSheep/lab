package cn.magicalsheep.wbclient.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyCard(
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    targetId: Int, // currently targetId is always blogId
    isReplyBlog: Boolean
) {

    var inputText by remember { mutableStateOf("") }
    var isRepost by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 8.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                modifier = Modifier
                    .requiredHeight(120.dp)
                    .fillMaxWidth(),
                singleLine = false,
                value = inputText,
                placeholder = {
                    if (isReplyBlog)
                        Text(text = stringResource(id = R.string.add_comment_for_blog))
                    else
                        Text(text = stringResource(id = R.string.add_comment_for_comment))
                },
                onValueChange = { inputText = it })
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isRepost, onCheckedChange = { isRepost = it })
                        Text(
                            text = stringResource(id = R.string.repost_at_the_same_time),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        scope.launch {
                            try {
                                homeViewModel.addComment(inputText, targetId)
                                if (isRepost)
                                    homeViewModel.addBlog(inputText, targetId)
                                snackbarHostState.showSnackbar("评论成功", withDismissAction = true)
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
                        Text(text = "发送")
                    }
                }
            }

        }
    }
}