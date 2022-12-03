package com.example.starbucksclone.view.login.signup

sealed class SignupEvent {
    data class IdentificationAgree(
        val isAgree: Boolean
    ): SignupEvent()

    data class ConditionComplete(
        val isComplete: Boolean
    ) : SignupEvent()

    object NextStep : SignupEvent()

    data class TextChange(
        val text: String,
        val type: String
    ) : SignupEvent()

    object NewCertificationNumber: SignupEvent()

    object Signup: SignupEvent()
}
