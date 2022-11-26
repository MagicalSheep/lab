package cn.magicalsheep.expressui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import cn.magicalsheep.expressui.data.UnauthorizedException
import cn.magicalsheep.expressui.data.entity.EncodedPackage
import cn.magicalsheep.expressui.data.entity.Package
import cn.magicalsheep.expressui.data.repository.packageRepository
import cn.magicalsheep.expressui.ui.LoginActivity
import cn.magicalsheep.expressui.ui.components.*
import cn.magicalsheep.expressui.ui.page.AppContent
import cn.magicalsheep.expressui.ui.theme.ExpressuiTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*

const val baseUrl = "https://express.magicalsheep.cn/"
const val INIT = 100
const val DECODING = 101
const val SUCCESS = 200
const val FAILED = 400
const val UNAUTHORIZED = 401

class MainActivity : ComponentActivity() {

    private var scanResult = mutableStateOf(Pair(INIT, "Init msg"))
    private var isLogin = mutableStateOf(false)
    private var isAdmin = mutableStateOf(false)
    private val snackbarHostState = SnackbarHostState()
    private var packageInfo = mutableStateOf(
        Package(
            id = 0,
            senderName = "Default",
            senderAddress = "Default",
            senderPhone = "Default",
            receiverName = "Default",
            receiverAddress = "Default",
            receiverPhone = "Default",
            description = "Default",
            weight = 0.0,
            freight = 0.0,
            position = "Default",
            sendTime = Date(),
            isRecv = false,
            recvTime = null
        )
    )
    private val handler = IHandler(WeakReference(this))
    private lateinit var sharedPreferences: SharedPreferences
    private val startLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CANCELED) return@registerForActivityResult
            // assert result code SUCCESS
            isLogin.value = true
            val role = sharedPreferences.getString("role", "default") ?: "default"
            isAdmin.value = (role == "admin")
            val loginSuccess = getString(R.string.login_success)
            lifecycleScope.launch {
                snackbarHostState.showSnackbar(loginSuccess, withDismissAction = true)
            }
        }

    private class IHandler(private val ref: WeakReference<MainActivity>) :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val activity = ref.get() ?: return
            when (msg.what) {
                SUCCESS -> {
                    val pack = msg.obj as Package
                    activity.packageInfo.value = pack
                    activity.scanResult.value = Pair(SUCCESS, "Get package successfully")
                }
                FAILED -> {
                    val errMsg = msg.obj as String
                    activity.scanResult.value = Pair(FAILED, errMsg)
                    activity.lifecycleScope.launch {
                        activity.snackbarHostState.showSnackbar(errMsg, withDismissAction = true)
                    }
                }
                UNAUTHORIZED -> {
                    activity.scanResult.value = Pair(INIT, "Init msg")
                    val note = msg.obj as String?
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.putExtra("msg", note)
                    activity.startLogin.launch(intent)
                }
            }
        }
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("token").apply()
        editor.remove("role").apply()
        isLogin.value = false
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        if (sharedPreferences.contains("token")) {
            isLogin.value = true
        }
        val role = sharedPreferences.getString("role", "default") ?: "default"
        isAdmin.value = (role == "admin")
        if (!isLogin.value) {
            handler.sendMessage(Message.obtain(null, UNAUTHORIZED, "请先登录"))
        }

        val startScanQrCode =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_CANCELED) return@registerForActivityResult
                val msg = it.data?.getStringExtra("msg") ?: "Unexpected branch"
                if (it.resultCode == FAILED) {
                    scanResult.value = Pair(FAILED, msg)
                    return@registerForActivityResult
                }
                scanResult.value = Pair(DECODING, msg)
                val token = sharedPreferences.getString("token", "") ?: ""
                Thread {
                    try {
                        val encodedPackage =
                            Gson().fromJson(scanResult.value.second, EncodedPackage::class.java)
                        // Get secret
                        val resp =
                            packageRepository.getSecret(
                                token = token,
                                packageId = encodedPackage.id
                            )
                                .execute().body()
                                ?: throw Exception("Error happened when fetch secret")
                        if (resp.status == UNAUTHORIZED) throw UnauthorizedException()
                        if (resp.status != SUCCESS) throw Exception(resp.message)
                        val secret = resp.data ?: ""
                        val pack = encodedPackage.decode(secret)
                        handler.sendMessage(Message.obtain(null, SUCCESS, pack))
                    } catch (ex: UnauthorizedException) {
                        handler.sendMessage(Message.obtain(null, UNAUTHORIZED, "登录信息已过期，请重新登录"))
                    } catch (ex: Exception) {
                        handler.sendMessage(Message.obtain(null, FAILED, ex.message))
                    }
                }.start()
            }

        setContent {
            ExpressuiTheme {

                val navController = rememberAnimatedNavController()
                val navigationActions = remember(navController) {
                    NavigationActions(navController)
                }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination =
                    navBackStackEntry?.destination?.route ?: Route.HOME

                Scaffold(topBar = {
                    AppBar(
                        context = this@MainActivity,
                        isLogin = isLogin,
                        startScanQrCode = startScanQrCode,
                        handler = handler,
                        logout = {
                            logout()
                            navigationActions.navigateTo(Route.HOME)
                        }
                    )
                }, bottomBar = {
                    if (isAdmin.value) {
                        BottomBar(
                            selectedDestination = selectedDestination,
                            navigateToTopLevelDestination = navigationActions::navigateTo
                        )
                    }
                }, snackbarHost = {
                    SnackbarHost(
                        modifier = Modifier.padding(0.dp, 8.dp),
                        hostState = snackbarHostState
                    )
                }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ) {
                        AppContent(
                            context = this,
                            navController = navController,
                            scanResult = scanResult,
                            packageInfo = packageInfo,
                            sharedPreferences = sharedPreferences,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}