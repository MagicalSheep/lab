package cn.magicalsheep.wbclient.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    user: User,
    doLogout: () -> Unit
) {
    var isUpdateCardVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .clickable { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
        ),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Avatar(
                    painter = if (user.name == stringResource(id = R.string.sheep_name)) {
                        painterResource(id = R.drawable.sheep_avatar)
                    } else {
                        painterResource(id = R.drawable.baseline_account_circle_24)
                    }, size = 80.dp, shape = CircleShape
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.moto) + ": " + user.moto,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { isUpdateCardVisible = !isUpdateCardVisible }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_profile)
                    )
                }
                IconButton(onClick = doLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = stringResource(id = R.string.logout)
                    )
                }
            }
            AnimatedVisibility(visible = isUpdateCardVisible) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var inputMotoText by remember { mutableStateOf("") }
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .weight(1f),
                        value = inputMotoText,
                        onValueChange = { inputMotoText = it },
                        placeholder = { Text(text = stringResource(id = R.string.new_moto)) },
                    )
                    IconButton(onClick = { updateProfile() }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(id = R.string.done)
                        )
                    }
                }
            }
        }
    }
}

fun updateProfile() {

}