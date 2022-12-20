package com.example.starbucksclone.view.main.order

sealed class OrderEvent {
    data class SelectChange(val select: String): OrderEvent()
}