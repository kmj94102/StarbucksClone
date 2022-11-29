package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToScreen(page: String) {
        navController.navigate(page)
    }

    companion object {
        const val Main = "main"
    }

}