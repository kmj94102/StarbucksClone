package com.example.starbucksclone.view.main.pay.list

sealed class CardListEvent {
    data class UpdateRepresentative(
        val cardNumber: String
    ): CardListEvent()
}