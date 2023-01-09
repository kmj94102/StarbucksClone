package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.MyMenuEntity
import javax.inject.Inject

class MyMenuClient @Inject constructor(
    private val dao: StarbucksDao
) {

    /** 나만의 메뉴 추가ㅏ **/
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

    /** 나만의 메뉴 삭제 **/
    suspend fun deleteMyMenu(
        index: Int,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.deleteMyMenu(index = index)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 나만의 메뉴 리스트 조회 **/
    fun selectMyMenuList(id: String) = dao.selectMyMenu(id)

}