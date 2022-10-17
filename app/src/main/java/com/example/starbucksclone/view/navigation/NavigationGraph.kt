package com.example.starbucksclone.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.example.starbucksclone.view.login.LoginScreen
import com.example.starbucksclone.view.login.sign_up.complete.SignUpCompleteScreen
import com.example.starbucksclone.view.login.sign_up.SignUpScreen
import com.example.starbucksclone.view.login.terms.TermsScreen
import com.example.starbucksclone.view.main.MainScreen
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
        startDestination = RoutAction.Login
    ) {
        /** 메인 화면 **/
        customComposable(
            route = RoutAction.Main
        ) {
            MainScreen(routAction = routAction)
        }
        /** 로그인 화면 **/
        customComposable(
            route = RoutAction.Login
        ) {
            LoginScreen(routAction = routAction)
        }
        /** 약관 동의 화면 **/
        customComposable(
            route = RoutAction.Terms
        ) {
            TermsScreen(routAction = routAction)
        }
        /** 회원가입 화면 **/
        customComposable(
            route = "${RoutAction.SignUp}/{isPush}",
            arguments = listOf(
                navArgument("isPush") { type = NavType.BoolType }
            )
        ) { entry ->
            val isPush = entry.arguments?.getBoolean("isPush") ?: false
            SignUpScreen(routAction = routAction, isPush = isPush)
        }
        /** 회원가입 완료 화면 **/
        customComposable(
            route = "${RoutAction.SignUpComplete}/{isPush}/{nickname}",
            arguments = listOf(
                navArgument("isPush") { type = NavType.BoolType },
                navArgument("nickname") { type = NavType.StringType },
            )
        ) { entry ->
            val isPush = entry.arguments?.getBoolean("isPush") ?: false
            val nickname = entry.arguments?.getString("nickname") ?: ""
            SignUpCompleteScreen(routAction = routAction, isPush = isPush, nickname = nickname)
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