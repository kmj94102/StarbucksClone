package com.example.starbucksclone.view.main.pay.card_detail

sealed class CardDetailEvent {
    data class ModifyCardName(var name: String): CardDetailEvent()
    object UpdateCardName: CardDetailEvent()
}
