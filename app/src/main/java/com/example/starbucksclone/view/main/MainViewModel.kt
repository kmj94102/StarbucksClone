package com.example.starbucksclone.view.main

import android.content.SharedPreferences
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.StarbucksDatabase
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.di.getDatabaseVersion
import com.example.starbucksclone.di.setDatabaseVersion
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
    private val repository: OrderMenuRepository
): ViewModel() {

    init {
        if (pref.getDatabaseVersion() < StarbucksDatabase.CurrentVersion) {
            val menuList = assetManager.readCSV(Constants.OrderCSV).map {
                OrderMenuEntity(
                    index = it[0],
                    name = it[1],
                    nameEng = it[2],
                    group = it[3],
                    image = it[4],
                    color = it[5]
                )
            }
            insertOrderMenu(menuList)
        }
    }

    private fun insertOrderMenu(
        menuList: List<OrderMenuEntity>
    ) = viewModelScope.launch {
        repository.insertOrderMenu(
            menuList = menuList,
            successListener = {
                Log.d("MainViewModel", "insert success")
                pref.setDatabaseVersion(StarbucksDatabase.CurrentVersion)
            },
            failureListener = {
                Log.d("MainViewModel", "insert failure")
            }
        )
    }

}