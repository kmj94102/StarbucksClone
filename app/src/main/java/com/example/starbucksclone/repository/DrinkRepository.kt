package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.DrinkClient
import com.example.starbucksclone.database.entity.DrinkDetailEntity
import com.example.starbucksclone.database.entity.DrinkEntity
import javax.inject.Inject

class DrinkRepository @Inject constructor(
    private val client: DrinkClient
) {

    suspend fun insertDrinks(
        drinkList: List<DrinkEntity>,
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
        detailList: List<DrinkDetailEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertDrinkDetails(
            detailList = detailList,
            successListener = successListener,
            failureListener = failureListener,
        )
    }

}