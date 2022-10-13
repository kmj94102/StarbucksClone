package com.example.starbucksclone.view.login.sign_up

sealed class SignUpEvent {

    object NextState: SignUpEvent()

    data class NewCertificationNumber(
        val newNumber: (String) -> Unit
    ): SignUpEvent()

    data class SelfAuthenticationResult(
        val name: String,
        val birthday: String,
        val phone: String
    ): SignUpEvent()

    data class IdPasswordResult(
        val id: String,
        val password: String
    ): SignUpEvent()

    data class EmailResult(
        val email: String
    ): SignUpEvent()

    data class NicknameResult(
        val nickname: String
    ): SignUpEvent()

    object Complete: SignUpEvent()

}