package cn.magicalsheep.wbclient.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import cn.magicalsheep.wbclient.R

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    painter: Painter,
    size: Dp,
    shape: Shape
) {
    Surface(
        modifier = modifier
            .requiredWidth(size)
            .requiredHeight(size),
        shape = shape
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.FillBounds,
            contentDescription = stringResource(id = R.string.avatar)
        )
    }
}