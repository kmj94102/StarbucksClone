package com.example.starbucksclone.view.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.view.main.gift.GiftScreen
import com.example.starbucksclone.view.main.home.HomeScreen
import com.example.starbucksclone.view.main.order.OrderScreen
import com.example.starbucksclone.view.main.other.OtherScreen
import com.example.starbucksclone.view.main.pay.PayScreen
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen(
    routAction: RoutAction,
    viewModel: MainViewModel = hiltViewModel()
) {
    // 스테이터스 바 색상 지정
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.White)
    systemUiController.statusBarDarkContentEnabled = true

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color(0xFFF8F8F8)
            ) {
                BottomNavigationContent(navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(it)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Pay.route) {
                PayScreen()
            }
            composable(Screen.Order.route) {
                OrderScreen()
            }
            composable(Screen.Gift.route) {
                GiftScreen()
            }
            composable(Screen.Other.route) {
                OtherScreen()
            }
        }
    }

}

@Composable
fun RowScope.BottomNavigationContent(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(Screen.Home, Screen.Pay, Screen.Order, Screen.Gift, Screen.Other)

    items.forEach { screen ->
        val currentRoute = navBackStackEntry?.destination?.route
        val selected = currentRoute == screen.route

        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = screen.icon),
                    contentDescription = screen.label
                )
            },
            label = {
                Text(
                    text = screen.label,
                    color = if (selected) MainColor else DarkGray,
                    fontSize = 12.sp
                )
            },
            unselectedContentColor = DarkGray,
            selectedContentColor = MainColor,
            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
            onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

sealed class Screen(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Pay : Screen("pay", "Pay", R.drawable.ic_pay)
    object Order : Screen("order", "Order", R.drawable.ic_order)
    object Gift : Screen("gift", "Gift", R.drawable.ic_gift)
    object Other : Screen("other", "Other", R.drawable.ic_other)
}