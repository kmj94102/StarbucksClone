package com.example.starbucksclone.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.example.starbucksclone.util.Constants
import com.example.starbucksclone.view.login.LoginScreen
import com.example.starbucksclone.view.login.signup.SignupScreen
import com.example.starbucksclone.view.login.signup.complete.SignupCompleteScreen
import com.example.starbucksclone.view.login.terms.TermsScreen
import com.example.starbucksclone.view.main.MainScreen
import com.example.starbucksclone.view.main.home.rewards.RewordScreen
import com.example.starbucksclone.view.main.order.cart.CartScreen
import com.example.starbucksclone.view.main.order.detail.MenuDetailScreen
import com.example.starbucksclone.view.main.order.menu_list.MenuListScreen
import com.example.starbucksclone.view.main.order.payment.PaymentScreen
import com.example.starbucksclone.view.main.order.search.MenuSearchScreen
import com.example.starbucksclone.view.main.order.search.result.MenuSearchResultScreen
import com.example.starbucksclone.view.main.pay.charging.ChargingScreen
import com.example.starbucksclone.view.main.pay.detail.CardDetailScreen
import com.example.starbucksclone.view.main.pay.list.CardListScreen
import com.example.starbucksclone.view.main.pay.registration.CardRegistrationScreen
import com.example.starbucksclone.view.main.pay.usage_history.UsageHistoryScreen
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
        /** ?????? ?????? **/
        customComposable(
            route = RouteAction.Main
        ) {
            MainScreen(routeAction = routeAction)
        }
        /** ????????? ?????? **/
        customComposable(
            route = RouteAction.Rewords
        ) {
            RewordScreen(routeAction = routeAction)
        }
        /** ????????? ?????? **/
        customComposable(
            route = RouteAction.Login
        ) {
            LoginScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? **/
        customComposable(
            route = RouteAction.Terms
        ) {
            TermsScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? **/
        customComposable(
            route = "${RouteAction.Signup}?{isPushConsent}",
            arguments = listOf(
                navArgument("isPushConsent") { type = NavType.BoolType }
            )
        ) {
            SignupScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? ?????? **/
        customComposable(
            route = RouteAction.SignupComplete
        ) {
            SignupCompleteScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = RouteAction.CardRegistration
        ) {
            CardRegistrationScreen(routeAction = routeAction)
        }
        /** ?????? ????????? ?????? **/
        customComposable(
            route = RouteAction.CardList
        ) {
            CardListScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = "${RouteAction.CardDetail}?{${Constants.CardNumber}}",
            arguments = listOf(
                navArgument(Constants.CardNumber) { type = NavType.StringType }
            )
        ) {
            CardDetailScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = "${RouteAction.CardCharging}?{${Constants.CardNumber}}",
            arguments = listOf(
                navArgument(Constants.CardNumber) { type = NavType.StringType }
            )
        ) {
            ChargingScreen(routeAction = routeAction)
        }
        /** ?????? ????????? ?????? **/
        customComposable(
            route = "${RouteAction.MenuList}?{${Constants.Group}},{${Constants.Name}}",
            arguments = listOf(
                navArgument(Constants.Group) { type = NavType.StringType },
                navArgument(Constants.Name) { type = NavType.StringType },
            )
        ) {
            MenuListScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = "${RouteAction.MenuDetail}?{${Constants.Indexes}}/{${Constants.Name}}",
            arguments = listOf(
                navArgument(Constants.Indexes) { type = NavType.StringType },
                navArgument(Constants.Name) { type = NavType.StringType }
            )
        ) {
            MenuDetailScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = RouteAction.MenuSearch
        ) {
            MenuSearchScreen(routeAction = routeAction)
        }
        /** ?????? ?????? ?????? **/
        customComposable(
            route = "${RouteAction.MenuSearchResult}/{${Constants.Value}}",
            arguments = listOf(
                navArgument(Constants.Value) { NavType.StringType }
            )
        ) {
            MenuSearchResultScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? **/
        customComposable(
            route = RouteAction.Cart
        ) {
            CartScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? (????????????) **/
        customComposable(
            route = RouteAction.Payment
        ) {
            PaymentScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? (??????) **/
        customComposable(
            route = "${RouteAction.Payment}/{${Constants.Item}}",
            arguments = listOf(
                navArgument(Constants.Item) { NavType.StringType }
            )
        ) {
            PaymentScreen(routeAction = routeAction)
        }
        /** ???????????? ?????? **/
        customComposable(
            route = "${RouteAction.UsageHistory}?{${Constants.CardNumber}}",
            arguments = listOf(
                navArgument(Constants.CardNumber) { NavType.StringType }
            )
        ) {
            UsageHistoryScreen(routeAction = routeAction)
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