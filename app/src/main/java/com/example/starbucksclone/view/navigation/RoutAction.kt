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
    fun goToOrderItem(indexes: String, type: String, color: String) {
        navController.navigate("$OrderItem/$indexes?$type,$color")
    }
    fun goToCardRegistration() {
        navController.navigate(CardRegistration)
    }
    fun goToCardList() {
        navController.navigate(CardList)
    }
    fun goToCardDetail(cardNumber: String) {
        navController.navigate("$CardDetail/$cardNumber")
    }

    companion object {
        const val Main = "main"
        const val Login = "login"
        const val Terms = "terms"
        const val SignUp = "sign_up"
        const val SignUpComplete = "sign_up_complete"
        const val OrderDetail = "order_detail"
        const val OrderItem = "order_item"
        const val CardRegistration = "card_registration"
        const val CardList = "card_list"
        const val CardDetail = "card_detail"
    }

}