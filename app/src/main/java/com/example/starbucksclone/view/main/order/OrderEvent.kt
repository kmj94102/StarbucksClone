package com.example.starbucksclone.view.main.order

sealed class OrderEvent {
    data class SelectGroup(val group: String): OrderEvent()
}
