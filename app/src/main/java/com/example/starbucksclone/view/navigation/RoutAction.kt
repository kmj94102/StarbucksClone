package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToTerms() {
        navController.navigate(Terms)
    }

    companion object {
        const val Login = "login"
        const val Terms = "terms"
    }

}