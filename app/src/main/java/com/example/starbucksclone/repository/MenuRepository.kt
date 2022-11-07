package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.MenuClient
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.database.entity.MenuEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val client: MenuClient
) {

    suspend fun insertDrinks(
        drinkList: List<MenuEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertDrink(
            drinkList = drinkList,
            successListener = successListener,
            failureListener = failureListener,
        )
    }

    suspend fun insertDrinkDetails(
        detailList: List<MenuDetailEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertDrinkDetails(
            detailList = detailList,
            successListener = successListener,
            failureListener = failureListener,
        )
    }

    fun selectDrinks(
        group: String,
    ) = client.selectDrinks(group)

    fun selectDrinkDetails(
        indexes: String
    ): Flow<List<MenuDetailEntity>> {
        val list = indexes.split(",")

        return try {
            client.selectDrinkDetail(
                firstIndex = list[0],
                secondIndex = if (list.size > 1) list[1] else ""
            )
        } catch (e: Exception) {
            emptyFlow()
        }
    }

}