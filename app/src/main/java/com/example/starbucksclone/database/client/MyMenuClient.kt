package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.MyMenuEntity
import javax.inject.Inject

class MyMenuClient @Inject constructor(
    private val dao: StarbucksDao
) {

    suspend fun insertMyMenu(
        myMenuEntity: MyMenuEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.insertMyMenu(myMenuEntity)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    fun selectMyMenuList(id: String) = dao.selectMyMenu(id)

}