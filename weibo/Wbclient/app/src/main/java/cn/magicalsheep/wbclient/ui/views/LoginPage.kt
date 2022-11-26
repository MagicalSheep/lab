package cn.magicalsheep.wbclient.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import cn.magicalsheep.wbclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onNavigationToHome: suspend () -> Unit
) {

    val loginUserState by profileViewModel.loginUserState.collectAsState()
    var account by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    val loginSuccess = stringResource(id = R.string.login_success)
    val registerSuccess = stringResource(id = R.string.register_success)

    Column(
        modifier = modifier,
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
            value = account,
            onValueChange = { account = it },
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.account)) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = pwd,
            onValueChange = { pwd = it },
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
                scope.launch {
                    try {
                        profileViewModel.login(account, pwd)
                        onNavigationToHome()
                        snackbarHostState.showSnackbar(loginSuccess, withDismissAction = true)
                    } catch (ex: Exception) {
                        ex.message?.let {
                            snackbarHostState.showSnackbar(
                                it,
                                withDismissAction = true
                            )
                        }
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.login))
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = {
                scope.launch {
                    try {
                        profileViewModel.register(account, pwd)
                        snackbarHostState.showSnackbar(
                            registerSuccess,
                            withDismissAction = true
                        )
                    } catch (ex: Exception) {
                        ex.message?.let {
                            snackbarHostState.showSnackbar(
                                it,
                                withDismissAction = true
                            )
                        }
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}