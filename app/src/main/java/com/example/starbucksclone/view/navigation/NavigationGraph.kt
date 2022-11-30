package com.example.starbucksclone.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.example.starbucksclone.view.main.MainScreen
import com.example.starbucksclone.view.main.home.rewards.RewordScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph() {
    val navController = rememberAnimatedNavController()
    val routAction = remember(RoutAction) { RoutAction(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = RoutAction.Main
    ) {
        /** 메인 화면 **/
        customComposable(
            route = RoutAction.Main
        ) {
            MainScreen(routAction = routAction)
        }
        /** 리워드 화면 **/
        customComposable(
            route = RoutAction.Rewords
        ) {
            RewordScreen(routAction = routAction)
        }
    }

}

fun getEnterTransition() =
    fadeIn() + slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium))

fun getExitTransition() =
    fadeOut() + slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium))

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.customComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        getEnterTransition()
    },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        getExitTransition()
    },
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        content = content
    )
}