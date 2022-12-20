package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RouteAction(private val navController: NavController) {

    fun getCurrentRoute(route: String) : Boolean =
        route == navController.currentDestination?.route

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToScreen(
        page: String,
        needPopupBackStack: Boolean = false
    ) {
        navController.navigate(page) {
            if (needPopupBackStack) {
                popupBackStack()
            }
        }
    }

    fun goToSignup(isPushConsent: Boolean) {
        navController.navigate("$Signup?$isPushConsent")
    }

    fun goToScreenWithCardNumber(page: String, cardNumber: String) {
        navController.navigate("$page?$cardNumber")
    }

    fun goToMenuList(group:String, name: String) {
        navController.navigate("${MenuList}?$group,$name")
    }

    fun goToMenuDetail(indexes: String) {
        navController.navigate("${MenuDetail}?$indexes")
    }

    fun goToMain() {
        navController.navigate(Main) {
            popUpTo(0)
        }
    }

    companion object {
        const val Main = "main"
        const val Rewords = "rewords"
        const val Login = "login"
        const val Terms = "terms"
        const val Signup = "signup"
        const val SignupComplete = "signup complete"
        const val CardRegistration = "card registration"
        const val CardList = "card list"
        const val CardDetail = "card detail"
        const val CardCharging = "card charging"
        const val MenuList = "menu list"
        const val MenuDetail = "menu detail"
    }

}