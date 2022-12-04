package com.example.starbucksclone.view.login

sealed class LoginEvent {
    object Login: LoginEvent()
    data class IdChange(
        val id: String
    ): LoginEvent()
    data class PasswordChange(
        val password: String
    ): LoginEvent()
}
