package com.example.starbucksclone.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.example.starbucksclone.view.login.LoginScreen
import com.example.starbucksclone.view.login.signup.SignupScreen
import com.example.starbucksclone.view.login.signup.complete.SignupCompleteScreen
import com.example.starbucksclone.view.login.terms.TermsScreen
import com.example.starbucksclone.view.main.MainScreen
import com.example.starbucksclone.view.main.home.rewards.RewordScreen
import com.example.starbucksclone.view.main.order.detail.MenuDetailScreen
import com.example.starbucksclone.view.main.order.menu_list.MenuListScreen
import com.example.starbucksclone.view.main.pay.PayScreen
import com.example.starbucksclone.view.main.pay.charging.ChargingScreen
import com.example.starbucksclone.view.main.pay.detail.CardDetailScreen
import com.example.starbucksclone.view.main.pay.list.CardListScreen
import com.example.starbucksclone.view.main.pay.registration.CardRegistrationScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph() {
    val navController = rememberAnimatedNavController()
    val routeAction = remember(RouteAction) { RouteAction(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = RouteAction.Main
    ) {
        /** 메인 화면 **/
        customComposable(
            route = RouteAction.Main
        ) {
            MainScreen(routeAction = routeAction)
        }
        /** 리워드 화면 **/
        customComposable(
            route = RouteAction.Rewords
        ) {
            RewordScreen(routeAction = routeAction)
        }
        /** 로그인 화면 **/
        customComposable(
            route = RouteAction.Login
        ) {
            LoginScreen(routeAction = routeAction)
        }
        /** 이용약관 화면 **/
        customComposable(
            route = RouteAction.Terms
        ) {
            TermsScreen(routeAction = routeAction)
        }
        /** 회원가입 화면 **/
        customComposable(
            route = "${RouteAction.Signup}?{isPushConsent}",
            arguments = listOf(
                navArgument("isPushConsent") { type = NavType.BoolType }
            )
        ) {
            SignupScreen(routeAction = routeAction)
        }
        /** 회원가입 완료 화면 **/
        customComposable(
            route = RouteAction.SignupComplete
        ) {
            SignupCompleteScreen(routeAction = routeAction)
        }
        /** 카드 등록 화면 **/
        customComposable(
            route = RouteAction.CardRegistration
        ) {
            CardRegistrationScreen(routeAction = routeAction)
        }
        /** 카드 리스트 화면 **/
        customComposable(
            route = RouteAction.CardList
        ) {
            CardListScreen(routeAction = routeAction)
        }
        /** 카드 상세 화면 **/
        customComposable(
            route = "${RouteAction.CardDetail}?{cardNumber}",
            arguments = listOf(
                navArgument("cardNumber") { type = NavType.StringType }
            )
        ) {
            CardDetailScreen(routeAction = routeAction)
        }
        /** 카드 충전 화면 **/
        customComposable(
            route = "${RouteAction.CardCharging}?{cardNumber}",
            arguments = listOf(
                navArgument("cardNumber") { type = NavType.StringType }
            )
        ) {
            ChargingScreen(routeAction = routeAction)
        }
        /** 메뉴 리스트 화면 **/
        customComposable(
            route = "${RouteAction.MenuList}?{group},{name}",
            arguments = listOf(
                navArgument("group") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
            )
        ) {
            MenuListScreen(routeAction = routeAction)
        }
        /** 메뉴 상세 화면 **/
        customComposable(
            route = RouteAction.MenuDetail
        ) {
            MenuDetailScreen(routeAction = routeAction)
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