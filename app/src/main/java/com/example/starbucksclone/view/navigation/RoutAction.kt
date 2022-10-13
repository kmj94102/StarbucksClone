package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToTerms() {
        navController.navigate(Terms)
    }
    fun goToSignUp() {
        navController.navigate(SignUp)
    }
    fun goToSignUpComplete() {
        navController.navigate(SignUpComplete)
    }

    companion object {
        const val Login = "login"
        const val Terms = "terms"
        const val SignUp = "sign_up"
        const val SignUpComplete = "sign_up_complete"
    }

}