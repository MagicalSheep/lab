package cn.magicalsheep.expressui.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cn.magicalsheep.expressui.R
import cn.magicalsheep.expressui.SUCCESS
import cn.magicalsheep.expressui.data.entity.UserDto
import cn.magicalsheep.expressui.data.repository.userRepository
import cn.magicalsheep.expressui.ui.theme.ExpressuiTheme
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences.Editor

    private suspend fun login(account: String, pwd: String) {
        val resp = userRepository.login(UserDto(account, pwd))
        if (resp.status != SUCCESS) {
            throw Exception(resp.message)
        }
        if (resp.data == null) {
            throw Exception("Bad response")
        }
        sharedPreferences.putString("token", resp.data)
        sharedPreferences.putString("role", resp.message)
        sharedPreferences.apply()
    }

    private suspend fun register(account: String, pwd: String) {
        if (account.isEmpty() || pwd.isEmpty()) {
            throw Exception("用户名或密码不能为空")
        }
        val resp = userRepository.register(UserDto(account, pwd))
        if (resp.status != SUCCESS) {
            throw Exception(resp.message)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE).edit()
        val registerSuccess = getString(R.string.register_success)
        val snackbarHostState = SnackbarHostState()
        val account = mutableStateOf("")
        val pwd = mutableStateOf("")
        val msg = intent.getStringExtra("msg")

        if (msg != null) {
            lifecycleScope.launch {
                snackbarHostState.showSnackbar(msg, withDismissAction = true)
            }
        }

        setContent {
            ExpressuiTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.welcome),
                                style = MaterialTheme.typography.displayLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedTextField(
                                value = account.value,
                                onValueChange = { account.value = it },
                                singleLine = true,
                                label = { Text(text = stringResource(id = R.string.account)) }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = pwd.value,
                                onValueChange = { pwd.value = it },
                                singleLine = true,
                                label = { Text(text = stringResource(id = R.string.password)) },
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(onClick = {
                                    lifecycleScope.launch {
                                        try {
                                            login(account.value, pwd.value)
                                            setResult(SUCCESS, Intent())
                                            finish()
                                        } catch (ex: Exception) {
                                            snackbarHostState.showSnackbar(
                                                ex.message ?: "Unknown exception",
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.login))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedButton(onClick = {
                                    lifecycleScope.launch {
                                        try {
                                            register(account.value, pwd.value)
                                            snackbarHostState.showSnackbar(
                                                registerSuccess,
                                                withDismissAction = true
                                            )
                                        } catch (ex: Exception) {
                                            snackbarHostState.showSnackbar(
                                                ex.message ?: "Unknown exception",
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.register))
                                }
                            }
                        }
                    }
                    SnackbarHost(
                        modifier = Modifier.padding(0.dp, 8.dp),
                        hostState = snackbarHostState
                    )
                }
            }
        }
    }
}