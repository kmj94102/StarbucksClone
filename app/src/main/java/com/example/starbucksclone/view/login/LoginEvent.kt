package com.example.starbucksclone.view.login

sealed class LoginEvent {
    data class IdChange(val id: String): LoginEvent()
    data class PwChange(val pw: String): LoginEvent()
    object Login: LoginEvent()
}
