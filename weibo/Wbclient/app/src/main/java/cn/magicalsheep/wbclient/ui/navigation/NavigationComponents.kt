package cn.magicalsheep.wbclient.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.data.User
import cn.magicalsheep.wbclient.ui.components.ProfileCard

fun isTopLevelPage(currentPage: String): Boolean {
    return currentPage == Route.HOME || currentPage == Route.PROFILE || currentPage == Route.SHEEP
}

@Composable
fun BottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    AnimatedVisibility(visible = isTopLevelPage(selectedDestination)) {
        NavigationBar(tonalElevation = 8.dp) {
            TOP_LEVEL_DESTINATIONS.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selectedDestination == item.route) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.route
                        )
                    },
                    selected = selectedDestination == item.route,
                    onClick = { navigateToTopLevelDestination(item) },
                    label = {
                        Text(
                            text = item.route,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selectedDestination == item.route) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    selectedDestination: String,
    navigationActions: NavigationActions,
    onNavigationToSettings: () -> Unit,
    onNavigationToAbout: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SmallTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
//            titleContentColor = MaterialTheme.colorScheme.onSurface
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                text = selectedDestination.replaceAfter("/", "").replace(Regex("/"), ""),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )

        },
        navigationIcon = {
            AnimatedVisibility(visible = !isTopLevelPage(selectedDestination)) {
                IconButton(onClick = { navigationActions.back() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.menu),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.settings)) },
                    onClick = {
                        expanded = false
                        onNavigationToSettings()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.about)) },
                    onClick = {
                        expanded = false
                        onNavigationToAbout()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = stringResource(id = R.string.about)
                        )
                    })
            }
        }
    )
}
