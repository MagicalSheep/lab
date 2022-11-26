package cn.magicalsheep.expressui.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cn.magicalsheep.expressui.DECODING
import cn.magicalsheep.expressui.SUCCESS
import cn.magicalsheep.expressui.data.entity.Package
import cn.magicalsheep.expressui.ui.components.EmptyContent
import cn.magicalsheep.expressui.ui.components.PackageInfo

@Composable
fun HomePage(
    scanResult: MutableState<Pair<Int, String>>,
    packageInfo: MutableState<Package>
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (scanResult.value.first) {
            DECODING -> {
                CircularProgressIndicator()
            }
            SUCCESS -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        PackageInfo(pack = packageInfo.value)
                    }
                }
            }
            else -> {
                EmptyContent()
            }
        }
    }
}