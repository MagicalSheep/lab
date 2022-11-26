package cn.magicalsheep.expressui.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cn.magicalsheep.expressui.R
import cn.magicalsheep.expressui.SUCCESS
import cn.magicalsheep.expressui.data.entity.Package
import cn.magicalsheep.expressui.data.repository.packageRepository
import cn.magicalsheep.expressui.ui.components.EditPackageInfo
import cn.magicalsheep.expressui.ui.theme.ExpressuiTheme
import kotlinx.coroutines.launch

class EditPackageActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val snackbarHostState = SnackbarHostState()

    private suspend fun add(pack: Package) {
        try {
            val token = sharedPreferences.getString("token", "") ?: ""
            val resp = packageRepository.addPackage(token, pack)
            if (resp.status != SUCCESS) {
                throw Exception(resp.message)
            }
            snackbarHostState.showSnackbar("添加快递成功", withDismissAction = true)
        } catch (ex: Exception) {
            snackbarHostState.showSnackbar(ex.message ?: "Unknown error")
        }
    }

    private suspend fun update(pack: Package) {
        try {
            val token = sharedPreferences.getString("token", "") ?: ""
            val resp = packageRepository.updatePackage(token, pack)
            if (resp.status != SUCCESS) {
                throw Exception(resp.message)
            }
            snackbarHostState.showSnackbar("修改快递信息成功", withDismissAction = true)
        } catch (ex: Exception) {
            snackbarHostState.showSnackbar(ex.message ?: "Unknown error")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)

        val obj = intent.getSerializableExtra("pack") ?: finish()
        val pack = obj as Package
        val isUpdate = (pack.id != 0L)

        setContent {
            ExpressuiTheme {
                Scaffold(topBar = {
                    SmallTopAppBar(
                        title = { Text(text = stringResource(id = R.string.edit_package)) },
                        navigationIcon = {
                            IconButton(
                                onClick = { finish() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "back"
                                )
                            }
                        })
                }, snackbarHost = {
                    SnackbarHost(
                        modifier = Modifier.padding(0.dp, 8.dp),
                        hostState = snackbarHostState
                    )
                }) {
                    EditPackageInfo(
                        modifier = Modifier.padding(it),
                        pack,
                        snackbarHostState
                    ) {
                        lifecycleScope.launch {
                            if (isUpdate) {
                                update(it)
                            } else {
                                add(it)
                            }
                        }
                    }
                }
            }
        }
    }
}