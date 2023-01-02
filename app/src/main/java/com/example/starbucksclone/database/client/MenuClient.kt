package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.database.entity.MenuSearchResult
import javax.inject.Inject

class MenuClient @Inject constructor(
    private val dao: StarbucksDao
) {
    /** 메뉴 등록 **/
    suspend fun insertMenu(
        menuList: List<MenuEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            dao.insertMenuList(
                menuList = menuList
            )
            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    /** 메뉴 상세 등록 **/
    suspend fun insertMenuDetails(
        detailList: List<MenuDetailEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            dao.insertMenuDetails(
                detailList = detailList
            )
            successListener()
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    /** 메뉴 조회 **/
    fun selectMenuList(
        group: String,
        name: String
    ) = dao.selectMenuList(group = group, name = name)

    /** New 메뉴 조회 **/
    fun selectNewMenuList(
        group: String
    ) = dao.selectNewMenuList(group)

    /** 추천 메뉴 조회 **/
    fun selectRecommendMenuList(
        group: String
    ) = dao.selectRecommendMenuList(group)

    /** 메뉴 상세 조회 **/
    suspend fun selectMenuDetail(
        indexList: List<String>,
        name: String
    ) = dao.selectMenuDetail(indexList = indexList, name = name)

    /** 검색 메뉴 조회 **/
    suspend fun selectSearchMenuList(
        group: String,
        name: String,
        successListener: (List<MenuSearchResult>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(
            dao.selectSearchMenuList(
                group = group,
                name = name
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 홈 화면 새로 나온 메뉴 조회 **/
    fun selectHomeNewMenuList() = dao.selectHomeNewMenuList()

}