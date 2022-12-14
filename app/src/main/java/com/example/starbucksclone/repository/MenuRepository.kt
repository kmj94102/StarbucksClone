package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.MenuClient
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.database.entity.MenuSearchResult
import com.example.starbucksclone.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val client: MenuClient
) {
    /** 메뉴 등록 **/
    suspend fun insertMenu(
        drinkList: List<MenuEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertMenu(
            menuList = drinkList,
            successListener = successListener,
            failureListener = failureListener,
        )
    }

    /** 메뉴 상세 조회 **/
    suspend fun insertMenuDetails(
        detailList: List<MenuDetailEntity>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertMenuDetails(
            detailList = detailList,
            successListener = successListener,
            failureListener = failureListener,
        )
    }

    /** 메뉴 조회 **/
    fun selectMenuList(
        group: String,
        name: String
    ) = client.selectMenuList(group, name)

    /** New 메뉴 조회 **/
    fun selectNewMenuList(
        group: String
    ) = client.selectNewMenuList(group)

    /** 추천 메뉴 조회**/
    fun selectRecommendMenuList(
        group: String
    ) = client.selectRecommendMenuList(group)

    /** 메뉴 상세 조회 **/
    suspend fun selectMenuDetails(
        indexes: String,
        name: String
    ) = client.selectMenuDetail(
        indexList = indexes.split(","),
        name = name
    ).map { it.mapper() }

    /** 검색 메뉴 조회 **/
    suspend fun selectMenuSearchList(
        group: String,
        name: String,
        successListener: (List<MenuSearchResult>) -> Unit,
        failureListener: () -> Unit
    ) {
        val searchGroup = when(group) {
            "음료" -> Constants.Drink
            "푸드" -> Constants.Food
            "상품" -> Constants.Product
            else -> ""
        }
        client.selectSearchMenuList(
            group = "%$searchGroup%",
            name = "%$name%",
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 홈 화면 새로 나온 메뉴 조회 **/
    fun selectHomeNewMenuList() = client.selectHomeNewMenuList()

}