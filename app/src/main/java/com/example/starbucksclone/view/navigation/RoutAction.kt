package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToScreen(page: String) {
        navController.navigate(page)
    }

    fun goToSignup(isPushConsent: Boolean) {
        navController.navigate("$Signup?$isPushConsent")
    }

    companion object {
        const val Main = "main"
        const val Rewords = "rewords"
        const val Login = "login"
        const val Terms = "terms"
        const val Signup = "signup"
        const val SignupComplete = "signup complete"
    }

}