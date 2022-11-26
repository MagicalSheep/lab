package cn.magicalsheep.expressui.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

object Route {
    const val HOME = "快递信息页"
    const val PACKAGES = "快递管理页"
    const val USERS = "用户管理页"
}

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = Route.HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    TopLevelDestination(
        route = Route.PACKAGES,
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email,
    ),
    TopLevelDestination(
        route = Route.USERS,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    )
)

fun isTopLevelPage(currentPage: String): Boolean {
    return currentPage == Route.HOME || currentPage == Route.PACKAGES || currentPage == Route.USERS
}

@Composable
fun BottomBar(
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

class NavigationActions(
    private val navController: NavHostController
) {

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState
            }
            launchSingleTop = true
            restoreState
        }
        navigateTo(destination.route)
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun <T> navigateToWithArgs(destination: String, args: List<T>) {
        var path = destination
        args.forEach { arg ->
            path += ("/" + arg.toString())
        }
        navigateTo(path)
    }

    fun back() {
        navController.popBackStack()
    }
}