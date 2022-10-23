package com.example.starbucksclone.view.navigation

import androidx.navigation.NavController

class RoutAction(private val navController: NavController) {

    fun popupBackStack() {
        navController.popBackStack()
    }

    fun goToMain() {
        navController.navigate(Main)
    }
    fun goToLogin() {
        navController.navigate(Login)
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
    fun goToOrderDetail(group: String, name: String) {
        navController.navigate("$OrderDetail/$group/$name")
    }

    companion object {
        const val Main = "main"
        const val Login = "login"
        const val Terms = "terms"
        const val SignUp = "sign_up"
        const val SignUpComplete = "sign_up_complete"
        const val OrderDetail = "order_detail"
    }

}