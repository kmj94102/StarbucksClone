package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.UsageHistoryClient
import com.example.starbucksclone.database.entity.UsageHistoryEntity
import javax.inject.Inject

class UsageHistoryRepository @Inject constructor(
    private val client: UsageHistoryClient
) {

    suspend fun insertUsageHistory(
        list: List<UsageHistoryEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = client.insertUsageHistory(
        items = list,
        successListener = successListener,
        failureListener = failureListener
    )

    fun selectUsageHistoryList(id: String, cardNumber: String) =
        client.selectUsageHistoryList(id = id, cardNumber = cardNumber)

}