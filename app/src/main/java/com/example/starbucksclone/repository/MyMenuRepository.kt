package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.MyMenuClient
import com.example.starbucksclone.database.entity.MyMenuEntity
import javax.inject.Inject

class MyMenuRepository @Inject constructor(
    private val client: MyMenuClient
) {

    /** 나만의 메뉴 추가 **/
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

    /** 나만의 메뉴 삭제 **/
    suspend fun deleteMyMenu(
        index: Int,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.deleteMyMenu(
            index = index,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 나만의 메뉴 리스트 조회 **/
    fun selectMyMenuList(id: String) = client.selectMyMenuList(id)
}