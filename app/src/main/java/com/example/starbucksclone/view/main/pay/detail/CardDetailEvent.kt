package com.example.starbucksclone.view.main.pay.detail

sealed class CardDetailEvent {
    data class CardNameChange(
        val cardName: String
    ): CardDetailEvent()

    object InitCardNameModify: CardDetailEvent()

    object CardNameModify: CardDetailEvent()

    object CardDelete: CardDetailEvent()
}
