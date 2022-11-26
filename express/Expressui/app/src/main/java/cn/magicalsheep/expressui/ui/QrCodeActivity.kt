package cn.magicalsheep.expressui.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import cn.magicalsheep.expressui.FAILED
import cn.magicalsheep.expressui.SUCCESS
import cn.magicalsheep.expressui.data.QrCodeAnalyser
import cn.magicalsheep.expressui.ui.components.EmptyContent
import cn.magicalsheep.expressui.ui.theme.ExpressuiTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import cn.magicalsheep.expressui.R

class QrCodeActivity : ComponentActivity() {

    private fun hideSystemUI() {
        // Hides the action bar at the top
        actionBar?.hide()
        // Hide the status bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpressuiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val cameraProviderFeature = remember {
                        ProcessCameraProvider.getInstance(context)
                    }
                    val cameraPermissionState = rememberPermissionState(
                        Manifest.permission.CAMERA
                    )
                    if (cameraPermissionState.status.isGranted) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                val preview = Preview.Builder().build()
                                val selector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                preview.setSurfaceProvider(previewView.surfaceProvider)
                                val imageAnalysis = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                                    .build()
                                imageAnalysis.setAnalyzer(
                                    ContextCompat.getMainExecutor(ctx),
                                    QrCodeAnalyser { result ->
                                        val intent = Intent()
                                        intent.putExtra("msg", result)
                                        setResult(SUCCESS, intent)
                                        finish()
                                    }
                                )
                                try {
                                    cameraProviderFeature.get().bindToLifecycle(
                                        lifecycleOwner,
                                        selector,
                                        preview,
                                        imageAnalysis
                                    )
                                } catch (e: Exception) {
                                    val intent = Intent()
                                    intent.putExtra("msg", e.message)
                                    setResult(FAILED, intent)
                                    finish()
                                }
                                previewView
                            }
                        )
                    } else {
                        EmptyContent(
                            title = stringResource(id = R.string.no_camera_perm_title),
                            subtitle = stringResource(
                                id = R.string.no_camera_perm_subtitle
                            )
                        )
                        LaunchedEffect(key1 = true) {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                }
            }
            hideSystemUI()
        }
    }
}