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
import cn.magicalsheep.expressui.data.entity.User

@Composable
fun UserInfo(
    user: User,
    addPermission: () -> Unit,
    removePermission: () -> Unit
) {
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
            PropItem(propKey = stringResource(id = R.string.account), propValue = user.account)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { addPermission() }) {
                    Text(
                        text = stringResource(id = R.string.add_permission),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                TextButton(
                    onClick = { removePermission() }) {
                    Text(
                        text = stringResource(id = R.string.remove_permission),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}