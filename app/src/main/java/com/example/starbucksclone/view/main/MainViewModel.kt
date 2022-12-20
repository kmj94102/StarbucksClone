package com.example.starbucksclone.view.main

import android.content.SharedPreferences
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.StarbucksDatabase
import com.example.starbucksclone.database.entity.*
import com.example.starbucksclone.di.getDatabaseVersion
import com.example.starbucksclone.di.setDatabaseVersion
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.repository.OrderMenuRepository
import com.example.starbucksclone.util.Constants
import com.example.starbucksclone.util.readCSV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val assetManager: AssetManager,
    private val pref: SharedPreferences,
    private val repository: OrderMenuRepository,
    private val drinkRepository: MenuRepository,
) : ViewModel() {

    init {
        if (pref.getDatabaseVersion(Constants.Order)
            < StarbucksDatabase.currentVersion(Constants.Order)
        ) {
            val menuList = assetManager.readCSV(Constants.OrderCSV).mapNotNull {
                createOrderMenuEntity(it)
            }
            insertOrderMenu(menuList)
        }

        if (pref.getDatabaseVersion(Constants.Drink)
            < StarbucksDatabase.currentVersion(Constants.Drink)
        ) {
            val drinkList = assetManager.readCSV(Constants.MenuCSV).mapNotNull {
                createDrinkEntity(it)
            }
            insertDrink(drinkList)

        }

        if (pref.getDatabaseVersion(Constants.DrinkDetail)
            < StarbucksDatabase.currentVersion(Constants.DrinkDetail)
        ) {
            val drinkDetailList = assetManager.readCSV(Constants.MenuDetailCSV).mapNotNull {
                createDrinkDetailEntity(it)
            }
            insertDrinkDetail(drinkDetailList)
        }

    }

    private fun insertOrderMenu(
        menuList: List<OrderMenuEntity>
    ) = viewModelScope.launch {
        repository.insertOrderMenu(
            menuList = menuList,
            successListener = {
                Log.d("MainViewModel", "insert Order success")
                pref.setDatabaseVersion(
                    Constants.Order,
                    StarbucksDatabase.currentVersion(Constants.Order)
                )
            },
            failureListener = {
                Log.d("MainViewModel", "insert failure")
            }
        )
    }

    private fun insertDrink(
        drinkList: List<MenuEntity>
    ) = viewModelScope.launch {
        drinkRepository.insertMenu(
            drinkList = drinkList,
            successListener = {
                Log.d("MainViewModel", "insert Drink success")
                pref.setDatabaseVersion(
                    Constants.Drink,
                    StarbucksDatabase.currentVersion(Constants.Drink)
                )
            },
            failureListener = {
                Log.d("MainViewModel", "insert failure")
            }
        )
    }

    private fun insertDrinkDetail(
        detailList: List<MenuDetailEntity>
    ) = viewModelScope.launch {
        drinkRepository.insertMenuDetails(
            detailList = detailList,
            successListener = {
                Log.d("MainViewModel", "insert DrinkDetail success")
                pref.setDatabaseVersion(
                    Constants.DrinkDetail,
                    StarbucksDatabase.currentVersion(Constants.DrinkDetail)
                )
            },
            failureListener = {
                Log.d("MainViewModel", "insert failure")
            }
        )
    }

}