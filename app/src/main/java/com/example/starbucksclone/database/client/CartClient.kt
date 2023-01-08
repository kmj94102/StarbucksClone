package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.CartEntity
import javax.inject.Inject

class CartClient @Inject constructor(
    private val dao: StarbucksDao
) {

    /** 장바구니 추가 **/
    suspend fun insertCardItem(
        cartEntity: CartEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.insertCartItem(cartEntity)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 장바구니 조회 **/
    fun selectCartItems(id: String) = dao.selectCartItems(id)

    /** 장바구니 조회 **/
    suspend fun selectCartItemList(
        id: String,
        successListener: (List<CartEntity>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.selectCartItemList(id))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 장바구니 카운트 조회 **/
    fun selectCartItemsCount(id: String) = dao.selectCartItemsCount(id)

    /** 장바구니 삭제 **/
    suspend fun deleteCartItem(
        index: Int,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.deleteCartItem(index)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 장바구니 전체삭제 **/
    suspend fun allDeleteCartItems(
        id: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.allDeleteCartItems(id)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}