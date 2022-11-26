package cn.magicalsheep.expressui.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.magicalsheep.expressui.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

@Composable
fun PackageInfo(
    isShort: Boolean = false,
    isShowDelete: Boolean = false,
    edit: () -> Unit = {},
    pack: cn.magicalsheep.expressui.data.entity.Package
) {
    val map = LinkedHashMap<String, String>()
    map[stringResource(id = R.string.package_id)] = pack.id.toString()
    if (!isShort) {
        map[stringResource(id = R.string.sender_name)] = pack.senderName
        map[stringResource(id = R.string.sender_address)] = pack.senderAddress
        map[stringResource(id = R.string.sender_phone)] = pack.senderPhone
        map[stringResource(id = R.string.receiver_name)] = pack.receiverName
        map[stringResource(id = R.string.receiver_address)] = pack.receiverAddress
        map[stringResource(id = R.string.receiver_phone)] = pack.receiverPhone
        map[stringResource(id = R.string.description)] = pack.description
        map[stringResource(id = R.string.weight)] = pack.weight.toString()
        map[stringResource(id = R.string.freight)] = pack.freight.toString()
        map[stringResource(id = R.string.position)] = pack.position
    }
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA)
    map[stringResource(id = R.string.send_time)] = simpleDateFormat.format(pack.sendTime)
    map[stringResource(id = R.string.is_recv)] = if (pack.isRecv) {
        stringResource(id = R.string.yes)
    } else {
        stringResource(id = R.string.no)
    }
    if (pack.isRecv && pack.recvTime != null) {
        map[stringResource(id = R.string.recv_time)] = simpleDateFormat.format(pack.recvTime)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 5.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape
    ) {
        Column {
            for ((propKey, propValue) in map) {
                PropItem(propKey = propKey, propValue = propValue)
            }
            if (isShowDelete) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { edit() }) {
                        Text(
                            text = stringResource(id = R.string.edit),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}