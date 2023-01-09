package com.example.starbucksclone.view.main.order

sealed class OrderEvent {
    data class SelectChange(val select: String): OrderEvent()
    data class Order(
        val index: Int
    ): OrderEvent()
    data class Cart(
        val index: Int
    ): OrderEvent()
    data class MyMenuDelete(
        val index: Int
    ): OrderEvent()
    object StatusInit: OrderEvent()
}