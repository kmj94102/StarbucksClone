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
    fun selectCardList(id: String) = dao.selectCardList(id)

    /** 대표 카드 존재 여부 체크 **/
    suspend fun selectCountRepresentative(
        id: String
    ): Int = try {
        dao.selectCountRepresentative(id)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }

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

    /** 카드 정보 조회 **/
    suspend fun selectCardInfo(
        cardNumber: String,
        successListener: (CardEntity) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.selectCardInfo(cardNumber = cardNumber))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 카드 이름 수정 **/
    suspend fun updateCardName(
        cardNumber: String,
        cardName: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.updateCardName(
            cardNumber = cardNumber,
            cardName = cardName
        )
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 카드 삭제 **/
    suspend fun deleteCard(
        cardNumber: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        dao.deleteCard(cardNumber)
        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}