package cn.magicalsheep.wbclient

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cn.magicalsheep.wbclient.ui.AppContent
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import cn.magicalsheep.wbclient.ui.navigation.BottomNavigationBar
import cn.magicalsheep.wbclient.ui.navigation.NavigationActions
import cn.magicalsheep.wbclient.ui.navigation.Route
import cn.magicalsheep.wbclient.ui.navigation.TopNavigationBar
import cn.magicalsheep.wbclient.ui.theme.WbclientTheme
import cn.magicalsheep.wbclient.ui.views.LoginPage
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WbclientTheme {
                val navController = rememberAnimatedNavController()
                val navigationActions = remember(navController) {
                    NavigationActions(navController)
                }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination =
                    navBackStackEntry?.destination?.route ?: Route.HOME

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var isNavBarVisible by remember { mutableStateOf(true) }

                val isLogin by profileViewModel.isLogin.collectAsState()

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        TopNavigationBar(
                            selectedDestination = selectedDestination,
                            navigationActions = navigationActions,
                            onNavigationToSettings = { navigationActions.navigateTo(Route.SETTINGS) },
                            onNavigationToAbout = { navigationActions.navigateTo(Route.ABOUT) }
                        )
                        AppContent(
                            navController = navController,
                            navigationActions = navigationActions,
                            homeViewModel = homeViewModel,
                            profileViewModel = profileViewModel,
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.inverseOnSurface),
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onPageDownScroll = { isNavBarVisible = false },
                            onPageUpScroll = { isNavBarVisible = true }
                        )
                        AnimatedVisibility(visible = isNavBarVisible) {
                            BottomNavigationBar(
                                selectedDestination = selectedDestination,
                                navigateToTopLevelDestination = navigationActions::navigateTo
                            )
                        }
                    }
                    AnimatedVisibility(visible = !isLogin) {
                        LoginPage(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            profileViewModel = profileViewModel,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onNavigationToHome = { navigationActions.navigateTo(Route.HOME) }
                        )
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