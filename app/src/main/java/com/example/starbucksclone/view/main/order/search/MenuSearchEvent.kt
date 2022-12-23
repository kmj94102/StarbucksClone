package com.example.starbucksclone.view.main.order.search

sealed class MenuSearchEvent {
    data class Search(
        val value: String
    ): MenuSearchEvent()

    data class DeleteHistory(
        val value: String
    ): MenuSearchEvent()

    object AllDelete: MenuSearchEvent()
}
