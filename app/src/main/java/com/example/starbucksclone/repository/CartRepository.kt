package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.CartClient
import com.example.starbucksclone.database.entity.CartEntity
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val client: CartClient
) {

    /** 장바구니 추가 **/
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

    /** 장바구니 조회 **/
    fun selectCartItems(id: String) = client.selectCartItems(id)

    /** 장바구니 조회 **/
    suspend fun selectCartItems(
        id: String,
        successListener: (List<CartEntity>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectCartItemList(
            id = id,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 장바구니 카운트 조회 **/
    fun selectCartItemsCount(id: String) = client.selectCartItemsCount(id = id)

    /** 장바구니 삭제 **/
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

    /** 장바구니 전체 삭제 **/
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