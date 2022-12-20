package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.OrderMenuClient
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderMenuRepository @Inject constructor(
    private val client: OrderMenuClient
) {
    /** Order 메뉴 아이템 등록 **/
    suspend fun insertOrderMenu(
        menuList: List<OrderMenuEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertOrderMenu(
            menu = menuList,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** Order 메뉴 조회 **/
    fun selectOrderMenu(
        group: String
    ): Flow<List<OrderMenuEntity>> {
        val selectGroup = when (group) {
            "푸드" -> Constants.Food
            "상품" -> Constants.Product
            else -> Constants.Drink
        }
        return client.selectOrderMenu(selectGroup)
    }

}