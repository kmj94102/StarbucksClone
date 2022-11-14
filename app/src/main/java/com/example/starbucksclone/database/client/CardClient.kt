package com.example.starbucksclone.database.client

import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.entity.CardEntity
import javax.inject.Inject

class CardClient @Inject constructor(
    private val dao: StarbucksDao
) {
    /** 카드 등록 **/
    suspend fun insertCard(
        cardEntity: CardEntity,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.insertCard(cardEntity = cardEntity)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 카드 리스트 조회 **/
    fun selectCardList() = dao.selectCardList()

    /** 대표카드 업데이트 **/
    suspend fun updateRepresentative(
        cardNumber: String,
        isRepresentative: Boolean,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.updateRepresentative(
            cardNumber = cardNumber,
            isRepresentative = isRepresentative
        )
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}