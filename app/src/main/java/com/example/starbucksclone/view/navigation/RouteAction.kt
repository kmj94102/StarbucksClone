package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RouteAction(private val navController: NavController) {

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
    }

}