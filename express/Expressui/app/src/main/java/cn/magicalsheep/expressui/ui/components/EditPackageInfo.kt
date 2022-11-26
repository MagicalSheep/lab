package cn.magicalsheep.expressui.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.magicalsheep.expressui.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPackageInfo(
    modifier: Modifier = Modifier,
    pack: cn.magicalsheep.expressui.data.entity.Package,
    snackbarHostState: SnackbarHostState,
    commit: (cn.magicalsheep.expressui.data.entity.Package) -> Unit,
) {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val scope = rememberCoroutineScope()

    var senderName by remember { mutableStateOf(pack.senderName) }
    var senderAddress by remember { mutableStateOf(pack.senderAddress) }
    var senderPhone by remember { mutableStateOf(pack.senderPhone) }
    var receiverName by remember { mutableStateOf(pack.receiverName) }
    var receiverAddress by remember { mutableStateOf(pack.receiverAddress) }
    var receiverPhone by remember { mutableStateOf(pack.receiverPhone) }
    var description by remember { mutableStateOf(pack.description) }
    var weight by remember { mutableStateOf(pack.weight.toString()) }
    var freight by remember { mutableStateOf(pack.freight.toString()) }
    var position by remember { mutableStateOf(pack.position) }
    var sendTime by remember { mutableStateOf(simpleDateFormat.format(pack.sendTime)) }
    var isRecv by remember { mutableStateOf(pack.isRecv) }
    var recvTime by remember { mutableStateOf(simpleDateFormat.format(pack.recvTime ?: Date())) }

    LazyColumn(modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = senderName,
                    onValueChange = { senderName = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.sender_name)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = senderAddress,
                    onValueChange = { senderAddress = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.sender_address)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = senderPhone,
                    onValueChange = { senderPhone = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.sender_phone)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = receiverName,
                    onValueChange = { receiverName = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.receiver_name)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = receiverAddress,
                    onValueChange = { receiverAddress = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.receiver_address)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = receiverPhone,
                    onValueChange = { receiverPhone = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.receiver_phone)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    singleLine = false,
                    label = {
                        Text(
                            text = stringResource(id = R.string.description)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = weight,
                    onValueChange = { weight = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.weight)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = freight,
                    onValueChange = { freight = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.freight)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = position,
                    onValueChange = { position = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.position)
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = sendTime,
                    onValueChange = { sendTime = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.send_time) + "（例: 2022-10-1 7:00:05）"
                        )
                    })
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.is_recv),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Checkbox(checked = isRecv, onCheckedChange = { isRecv = it })
            }
        }
        if (isRecv) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = recvTime,
                        onValueChange = { recvTime = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = stringResource(id = R.string.recv_time) + "（例: 2022-10-1 7:00:05）"
                            )
                        })
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val ch1 = stringResource(id = R.string.sender_name_cannot_be_empty)
                val ch2 = stringResource(id = R.string.sender_address_cannot_be_empty)
                val ch3 = stringResource(id = R.string.sender_phone_cannot_be_empty)
                val ch4 = stringResource(id = R.string.receiver_name_cannot_be_empty)
                val ch5 = stringResource(id = R.string.receiver_address_cannot_be_empty)
                val ch6 = stringResource(id = R.string.receiver_phone_cannot_be_empty)
                val ch7 = stringResource(id = R.string.description_cannot_be_empty)
                val ch8 = stringResource(id = R.string.position_cannot_be_empty)
                Button(
                    modifier = Modifier.padding(16.dp, 8.dp),
                    onClick = {
                        if (senderName.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch1,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (senderAddress.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch2,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (senderPhone.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch3,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (receiverName.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch4,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (receiverAddress.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch5,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (receiverPhone.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch6,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (description.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch7,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        if (position.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ch8,
                                    withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        val weightD = try {
                            weight.toDouble()
                        } catch (ex: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ex.message ?: "Unknown error", withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        val freightD = try {
                            freight.toDouble()
                        } catch (ex: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ex.message ?: "Unknown error", withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        val sendTimeD = try {
                            simpleDateFormat.parse(sendTime)
                        } catch (ex: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    ex.message ?: "Unknown error", withDismissAction = true
                                )
                            }
                            return@Button
                        }
                        var recvTimeD: Date? = null
                        if (isRecv) {
                            recvTimeD = try {
                                simpleDateFormat.parse(recvTime)
                            } catch (ex: Exception) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        ex.message ?: "Unknown error", withDismissAction = true
                                    )
                                }
                                return@Button
                            }
                        }

                        val ret = cn.magicalsheep.expressui.data.entity.Package(
                            id = pack.id,
                            senderName,
                            senderAddress,
                            senderPhone,
                            receiverName,
                            receiverAddress,
                            receiverPhone,
                            description,
                            weight = weightD,
                            freight = freightD,
                            position,
                            sendTime = sendTimeD,
                            isRecv,
                            recvTime = recvTimeD
                        )
                        commit(ret)
                    }) {
                    Text(text = stringResource(id = R.string.completed))
                }
            }
        }
    }

}