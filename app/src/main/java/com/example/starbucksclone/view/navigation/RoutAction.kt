package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToTerms() {
        navController.navigate(Terms)
    }
    fun goToSignUp(isPush: Boolean) {
        navController.navigate("$SignUp/$isPush") {
            popupBackStack()
        }
    }
    fun goToSignUpComplete(isPush: Boolean, nickname: String) {
        navController.navigate("$SignUpComplete/$isPush/$nickname") {
            popupBackStack()
        }
    }

    companion object {
        const val Login = "login"
        const val Terms = "terms"
        const val SignUp = "sign_up"
        const val SignUpComplete = "sign_up_complete"
    }

}