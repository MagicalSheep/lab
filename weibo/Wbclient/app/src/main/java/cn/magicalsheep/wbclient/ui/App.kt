package cn.magicalsheep.wbclient.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import cn.magicalsheep.wbclient.R
import cn.magicalsheep.wbclient.ui.components.EmptyPage
import cn.magicalsheep.wbclient.ui.model.HomeViewModel
import cn.magicalsheep.wbclient.ui.model.ProfileViewModel
import cn.magicalsheep.wbclient.ui.navigation.NavigationActions
import cn.magicalsheep.wbclient.ui.navigation.Route
import cn.magicalsheep.wbclient.ui.views.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent(
    navController: NavHostController,
    navigationActions: NavigationActions,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onPageUpScroll: () -> Unit,
    onPageDownScroll: () -> Unit
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.HOME,
    ) {
        composable(Route.HOME) {
            HomePage(
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                onNavigationToDetail = { blogId ->
                    navigationActions.navigateToWithArgs(
                        Route.DETAIL,
                        listOf(blogId)
                    )
                },
                onNavigationToEditBlog = { navigationActions.navigateTo(Route.EDIT) },
                onPageUpScroll = onPageUpScroll,
                onPageDownScroll = onPageDownScroll,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
        composable(Route.PROFILE) {
            ProfilePage(
                profileViewModel = profileViewModel,
                onNavigationToDetail = { blogId ->
                    navigationActions.navigateToWithArgs(
                        Route.DETAIL,
                        listOf(blogId)
                    )
                },
                onNavigationToEditBlog = { navigationActions.navigateTo(Route.EDIT) },
                snackbarHostState = snackbarHostState, scope = scope,
                onNavigationToHome = { navigationActions.navigateTo(Route.HOME) },
                onPageUpScroll = onPageUpScroll,
                onPageDownScroll = onPageDownScroll,
                homeViewModel = homeViewModel
            )
        }
        composable(Route.SHEEP) {
            SheepBlogPage()
        }
        composable(
            Route.DETAIL + "/{blogId}",
            arguments = listOf(navArgument("blogId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }
        ) {
            val blogId = it.arguments?.getInt("blogId")
            if (blogId == null) {
                EmptyPage(
                    title = stringResource(id = R.string.error_title), subtitle = stringResource(
                        id = R.string.error_subtitle
                    )
                )
            } else {
                homeViewModel.setBlogDetailState()
                BlogDetailPage(blogId, homeViewModel, snackbarHostState, scope)
            }
        }
        composable(
            Route.EDIT,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }) {
            EditBlogPage(
                homeViewModel = homeViewModel,
                snackbarHostState = snackbarHostState,
                scope = scope,
                onNavigationBack = { navigationActions.back() })
        }
        composable(
            Route.ABOUT,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }) {
            AboutPage()
        }
        composable(
            Route.SETTINGS,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }) {
            SettingsPage()
        }
    }
}
