package com.example.starbucksclone.view.main.order.search.result

sealed class MenuSearchResultEvent {
    data class SelectedChange(
        val value: String
    ): MenuSearchResultEvent()
}
