package cn.magicalsheep.expressui.ui.components

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cn.magicalsheep.expressui.ui.QrCodeActivity
import cn.magicalsheep.expressui.UNAUTHORIZED
import cn.magicalsheep.expressui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    context: Context,
    isLogin: MutableState<Boolean>,
    startScanQrCode: ActivityResultLauncher<Intent>,
    handler: Handler,
    logout: () -> Unit
) {
    SmallTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = {
                val intent = Intent(context, QrCodeActivity::class.java)
                startScanQrCode.launch(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Scan QR code"
                )
            }
            if (isLogin.value) {
                IconButton(onClick = {
                    logout()
                    handler.sendMessage(
                        Message.obtain(
                            null,
                            UNAUTHORIZED,
                            "成功退出登录"
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Logout"
                    )
                }
            } else {
                IconButton(onClick = {
                    handler.sendMessage(
                        Message.obtain(
                            null,
                            UNAUTHORIZED
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_login_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Logout"
                    )
                }
            }
        }
    )
}
