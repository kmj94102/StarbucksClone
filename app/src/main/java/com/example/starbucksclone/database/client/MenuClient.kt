package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.database.entity.MenuEntity
import javax.inject.Inject

class MenuClient @Inject constructor(
    private val dao: StarbucksDao
) {
    suspend fun insertDrink(
        drinkList: List<MenuEntity>,
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
        detailList: List<MenuDetailEntity>,
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

    fun selectDrinks(
        group: String,
    ) = dao.selectDrinks(group = group)

    fun selectDrinkDetail(
        firstIndex: String,
        secondIndex: String
    ) = dao.selectDrinkDetail(firstIndex = firstIndex, secondIndex = secondIndex)

}