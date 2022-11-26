package cn.magicalsheep.expressui.ui.page

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.magicalsheep.expressui.ui.EditPackageActivity
import cn.magicalsheep.expressui.R
import cn.magicalsheep.expressui.data.entity.Package
import cn.magicalsheep.expressui.ui.components.PackageInfo
import cn.magicalsheep.expressui.ui.components.SwipeRefreshList
import cn.magicalsheep.expressui.ui.paging.PackageSource
import java.util.*

@Composable
fun PackagesPage(
    context: Context,
    sharedPreferences: SharedPreferences
) {

    val scope = rememberCoroutineScope()
    val packageList = remember {
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20)
        ) {
            PackageSource(sharedPreferences)
        }.flow.cachedIn(scope)
    }
    val packageLazyPagingItems = packageList.collectAsLazyPagingItems()
    val isRefreshing = packageLazyPagingItems.loadState.refresh is LoadState.Loading
            && packageLazyPagingItems.itemCount >= 0
    val isLoadingMore = packageLazyPagingItems.loadState.append is LoadState.Loading

    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefreshList(
            isLoadingMore = isLoadingMore,
            isRefreshing = isRefreshing,
            onRefresh = { packageLazyPagingItems.refresh() }
        ) {
            items(packageLazyPagingItems) { pack ->
                pack?.let {
                    PackageInfo(isShort = true, isShowDelete = true, pack = it, edit = {
                        val intent = Intent(context, EditPackageActivity::class.java)
                        intent.putExtra("pack", it)
                        context.startActivity(intent)
                    })
                }
            }
            if (packageLazyPagingItems.loadState.append.endOfPaginationReached) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(24.dp),
                            text = stringResource(id = R.string.end_of_packages),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
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
                onClick = {
                    val intent = Intent(context, EditPackageActivity::class.java)
                    intent.putExtra(
                        "pack", Package(
                            id = 0,
                            senderName = "",
                            senderAddress = "",
                            senderPhone = "",
                            receiverName = "",
                            receiverAddress = "",
                            receiverPhone = "",
                            description = "",
                            weight = 0.0,
                            freight = 0.0,
                            position = "",
                            sendTime = Date(),
                            isRecv = false
                        )
                    )
                    context.startActivity(intent)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_package)
                )
            }
        }
    }
}