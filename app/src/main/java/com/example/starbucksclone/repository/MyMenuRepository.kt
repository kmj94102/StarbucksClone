package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.MyMenuClient
import com.example.starbucksclone.database.entity.MyMenuEntity
import javax.inject.Inject

class MyMenuRepository @Inject constructor(
    private val client: MyMenuClient
) {

    suspend fun insertMyMenu(
        myMenuEntity: MyMenuEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertMyMenu(
            myMenuEntity = myMenuEntity,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    fun selectMyMenuList(id: String) = client.selectMyMenuList(id)
}