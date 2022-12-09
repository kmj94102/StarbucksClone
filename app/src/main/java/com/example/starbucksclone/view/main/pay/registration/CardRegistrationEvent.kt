package com.example.starbucksclone.view.main.pay.registration

sealed class CardRegistrationEvent {
    data class TextChange(
        val text: String,
        val type: String
    ) : CardRegistrationEvent()

    data class ModalChange(
        val value: Int
    ) : CardRegistrationEvent()

    data class BarcodeRegistration(
        val number: String
    ) : CardRegistrationEvent()

    object CouponRegistration: CardRegistrationEvent()

    object TermsCheck : CardRegistrationEvent()
    object CardRegistration : CardRegistrationEvent()
}
