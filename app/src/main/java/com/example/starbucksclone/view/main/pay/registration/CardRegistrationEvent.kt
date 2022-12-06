package com.example.starbucksclone.view.main.pay.registration

sealed class CardRegistrationEvent {
    data class TextChange(
        val text: String,
        val type: String
    ): CardRegistrationEvent()

    object TermsCheck: CardRegistrationEvent()
    object CardRegistration: CardRegistrationEvent()
}
