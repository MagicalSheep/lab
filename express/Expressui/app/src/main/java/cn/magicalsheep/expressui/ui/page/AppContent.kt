package cn.magicalsheep.expressui.ui.page

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import cn.magicalsheep.expressui.ui.components.EmptyContent
import cn.magicalsheep.expressui.ui.components.Route
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent(
    context: Context,
    navController: NavHostController,
    scanResult: MutableState<Pair<Int, String>>,
    packageInfo: MutableState<cn.magicalsheep.expressui.data.entity.Package>,
    sharedPreferences: SharedPreferences,
    snackbarHostState: SnackbarHostState
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Route.HOME,
    ) {
        composable(Route.HOME) {
            HomePage(scanResult = scanResult, packageInfo = packageInfo)
        }
        composable(Route.PACKAGES) {
            PackagesPage(
                context = context,
                sharedPreferences = sharedPreferences
            )
        }
        composable(Route.USERS) {
            UsersPage(sharedPreferences = sharedPreferences, snackbarHostState = snackbarHostState)
        }
    }
}