package cn.magicalsheep.wbclient.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.ui.components.EmptyPage
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBlogPage(
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onNavigationBack: () -> Unit
) {

    val maxLength = 140
    var blogContent by remember { mutableStateOf("") }
    var remainLength by remember { mutableStateOf(maxLength) }
    val developNote = stringResource(id = R.string.develop_note)

    Column {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            value = blogContent,
            placeholder = { Text(text = stringResource(id = R.string.say_something)) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Unspecified,
                unfocusedIndicatorColor = Color.Unspecified
            ),
            onValueChange = {
                blogContent = it
                remainLength = maxLength - blogContent.length
            })
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            developNote,
                            withDismissAction = true
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = R.string.image)
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
                        painter = painterResource(id = R.drawable.baseline_emoji_emotions_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(id = R.string.emoji)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = remainLength.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.weight(1f, true))
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp, 12.dp),
                    onClick = {
                        scope.launch {
                            try {
                                homeViewModel.addBlog(blogContent, null)
                                onNavigationBack()
                                snackbarHostState.showSnackbar("发布微博成功", withDismissAction = true)
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
                        imageVector = Icons.Default.Send,
                        contentDescription = stringResource(id = R.string.add_blog)
                    )
                }
            }
        }
    }
}