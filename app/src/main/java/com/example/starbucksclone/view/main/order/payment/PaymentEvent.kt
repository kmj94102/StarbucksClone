package com.example.starbucksclone.view.main.order.payment

sealed class PaymentEvent {
    data class ModalStateChange(
        val value: Int
    ): PaymentEvent()
    data class SelectCardChange(
        val cardNumber: String
    ): PaymentEvent()
    data class Payment(
        val totalPrice: Int
    ): PaymentEvent()
}
