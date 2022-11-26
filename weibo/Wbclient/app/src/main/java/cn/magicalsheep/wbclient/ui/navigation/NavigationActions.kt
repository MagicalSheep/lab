package cn.magicalsheep.wbclient.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel

object Route {
    const val PROFILE = "个人主页"
    const val HOME = "首页"
    const val SHEEP = "开发者博客"
    const val DETAIL = "微博详情"
    const val EDIT = "编辑微博"
    const val ABOUT = "关于"
    const val SETTINGS = "设置"
}

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

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

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = Route.PROFILE,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
    TopLevelDestination(
        route = Route.HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    TopLevelDestination(
        route = Route.SHEEP,
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
    )
)
