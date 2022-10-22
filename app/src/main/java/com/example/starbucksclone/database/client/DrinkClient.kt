package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.DrinkDetailEntity
import com.example.starbucksclone.database.entity.DrinkEntity
import javax.inject.Inject

class DrinkClient @Inject constructor(
    private val dao: StarbucksDao
) {
    suspend fun insertDrink(
        drinkList: List<DrinkEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            dao.insertDrinks(
                drinkList = drinkList
            )
            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    suspend fun insertDrinkDetails(
        detailList: List<DrinkDetailEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            dao.insertDrinkDetails(
                detailList = detailList
            )
            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

}