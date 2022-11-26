package cn.magicalsheep.wbclient.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.ui.components.EmptyPage

@Composable
fun AboutPage(
    modifier: Modifier = Modifier
) {
    EmptyPage(
        modifier = modifier,
        title = stringResource(id = R.string.about_title),
        subtitle = stringResource(id = R.string.about_content)
    )
}