package com.example.starbucksclone.view.main.order.cart

sealed class CartEvent {
    data class AmountChange(
        val value: Int,
        val index: Int
    ): CartEvent()

    data class DeleteCartItem(
        val index: Int
    ): CartEvent()

    object AllDeleteCartItems: CartEvent()
}
