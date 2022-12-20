package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.OrderMenuEntity
import javax.inject.Inject

class OrderMenuClient @Inject constructor(
    private val dao: StarbucksDao
) {

    /** Order 메뉴 아이템 등록 **/
    suspend fun insertOrderMenu(
        menu: List<OrderMenuEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            dao.insertOrderMenu(menu)
            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }
    /** Order 메뉴 조회 **/
    fun selectOrderMenu(group: String) =
        dao.selectOrderMenu(group)

}