package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.CartClient
import com.example.starbucksclone.database.entity.CartEntity
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val client: CartClient
) {

    suspend fun insertCartItem(
        cartEntity: CartEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertCardItem(
            cartEntity = cartEntity,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    fun selectCartItems(id: String) = client.selectCartItems(id)

    suspend fun deleteCartItem(
        index: Int,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.deleteCartItem(
            index = index,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun allDeleteCartItems(
        id: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.allDeleteCartItems(
            id = id,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}