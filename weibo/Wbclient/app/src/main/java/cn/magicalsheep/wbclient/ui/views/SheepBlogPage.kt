package cn.magicalsheep.wbclient.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.magicalsheep.wbclient.R
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SheepBlogPage() {

    val state = rememberWebViewState(stringResource(id = R.string.sheep_blog_url))

    Surface(modifier = Modifier.fillMaxSize()) {
        WebView(state, onCreated = { it.settings.javaScriptEnabled = true })
    }
}