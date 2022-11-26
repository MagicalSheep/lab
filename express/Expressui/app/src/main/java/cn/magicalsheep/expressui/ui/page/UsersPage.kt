package cn.magicalsheep.expressui.ui.page

import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.expressui.R
import cn.magicalsheep.expressui.SUCCESS
import cn.magicalsheep.expressui.data.repository.userRepository
import cn.magicalsheep.expressui.ui.components.SwipeRefreshList
import cn.magicalsheep.expressui.ui.components.UserInfo
import cn.magicalsheep.expressui.ui.paging.UserSource
import kotlinx.coroutines.launch

suspend fun addPermission(token: String, account: String, packId: Long) {
    val resp = userRepository.addPermission(token, account, packageId = packId)
    if (resp.status != SUCCESS) {
        throw Exception(resp.message)
    }
}

suspend fun removePermission(token: String, account: String, packId: Long) {
    val resp = userRepository.removePermission(token, account, packageId = packId)
    if (resp.status != SUCCESS) {
        throw Exception(resp.message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersPage(
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences
) {
    val scope = rememberCoroutineScope()
    val userList = remember {
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20)
        ) {
            UserSource(sharedPreferences)
        }.flow.cachedIn(scope)
    }
    val userLazyPagingItems = userList.collectAsLazyPagingItems()
    val isRefreshing = userLazyPagingItems.loadState.refresh is LoadState.Loading
            && userLazyPagingItems.itemCount >= 0
    val isLoadingMore = userLazyPagingItems.loadState.append is LoadState.Loading
    var showAddPermDialog by remember { mutableStateOf(false) }
    var showRemovePermDialog by remember { mutableStateOf(false) }
    var targetUserId by remember { mutableStateOf("") }
    var targetPackId by remember { mutableStateOf("") }
    val addSuccess = stringResource(id = R.string.add_permission_success)
    val removeSuccess = stringResource(id = R.string.remove_permission_success)

    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefreshList(
            isLoadingMore = isLoadingMore,
            isRefreshing = isRefreshing,
            onRefresh = { userLazyPagingItems.refresh() }
        ) {
            items(userLazyPagingItems) { user ->
                user?.let {
                    UserInfo(user = user, addPermission = {
                        targetUserId = user.account
                        showAddPermDialog = true
                    }) {
                        targetUserId = user.account
                        showRemovePermDialog = true
                    }
                }
            }
            if (userLazyPagingItems.loadState.append.endOfPaginationReached) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(24.dp),
                            text = stringResource(id = R.string.end_of_users),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
        if (showAddPermDialog) {
            Dialog(onDismissRequest = {}) {
                Card {
                    Column(modifier = Modifier.padding(16.dp, 16.dp)) {
                        Text(
                            text = stringResource(id = R.string.add_permission),
                            modifier = Modifier.padding(8.dp, 8.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp, 8.dp),
                            value = targetUserId,
                            onValueChange = { targetUserId = it },
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.add_perm_user_label))
                            })
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp, 8.dp),
                            value = targetPackId,
                            onValueChange = { targetPackId = it },
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.add_perm_label))
                            })
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = {
                                showAddPermDialog = false
                            }) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            TextButton(onClick = {
                                showAddPermDialog = false
                                scope.launch {
                                    try {
                                        val packId = targetPackId.toLong()
                                        val token = sharedPreferences.getString("token", "") ?: ""
                                        addPermission(token, targetUserId, packId)
                                        snackbarHostState.showSnackbar(
                                            addSuccess,
                                            withDismissAction = true
                                        )
                                    } catch (ex: Exception) {
                                        snackbarHostState.showSnackbar(
                                            ex.message ?: "Unknown error", withDismissAction = true
                                        )
                                    }
                                }
                            }) {
                                Text(text = stringResource(id = R.string.completed))
                            }
                        }

                    }
                }

            }
        }
        if (showRemovePermDialog) {
            Dialog(onDismissRequest = {}) {
                Card {
                    Column(modifier = Modifier.padding(16.dp, 16.dp)) {
                        Text(
                            text = stringResource(id = R.string.remove_permission),
                            modifier = Modifier.padding(8.dp, 8.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp, 8.dp),
                            value = targetUserId,
                            onValueChange = { targetUserId = it },
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.remove_perm_user_label))
                            })
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp, 8.dp),
                            value = targetPackId,
                            onValueChange = { targetPackId = it },
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.remove_perm_label))
                            })
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = {
                                showRemovePermDialog = false
                            }) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            TextButton(onClick = {
                                showRemovePermDialog = false
                                scope.launch {
                                    try {
                                        val packId = targetPackId.toLong()
                                        val token = sharedPreferences.getString("token", "") ?: ""
                                        removePermission(token, targetUserId, packId)
                                        snackbarHostState.showSnackbar(
                                            removeSuccess,
                                            withDismissAction = true
                                        )
                                    } catch (ex: Exception) {
                                        snackbarHostState.showSnackbar(
                                            ex.message ?: "Unknown error", withDismissAction = true
                                        )
                                    }
                                }
                            }) {
                                Text(text = stringResource(id = R.string.completed))
                            }
                        }

                    }
                }

            }
        }
    }


}