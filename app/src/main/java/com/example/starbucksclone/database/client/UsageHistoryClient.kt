package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.UsageHistoryEntity
import javax.inject.Inject

class UsageHistoryClient @Inject constructor(
    private val dao: StarbucksDao
) {

    suspend fun insertUsageHistory(
        items: List<UsageHistoryEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.insertUsageHistory(*items.toTypedArray())
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    fun selectUsageHistoryList(id: String, cardNumber: String) =
        dao.selectUsageHistoryList(id, cardNumber)

}