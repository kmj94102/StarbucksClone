package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.CardClient
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.util.getStarbucksCardImage
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val client: CardClient
) {

    /** 카드 등록 **/
    suspend fun createCard(
        id: String,
        info: CardRegistrationInfo,
        successListener: () -> Unit,
        failureListener: (String) -> Unit
    ) {
        if (id.isEmpty()) {
            failureListener("로그인 후 이용해주세요.")
            return
        }

        val isRepresentative = client.selectCountRepresentative(id) == 0

        client.insertCard(
            cardEntity = CardEntity(
                id = id,
                cardName = info.cardName.ifEmpty { "스타벅스 카드" },
                cardNumber = info.cardNumber,
                cardImage = getStarbucksCardImage(),
                pinNumber = info.pinNumber,
                balance = 0,
                representative = isRepresentative
            ),
            successListener = successListener,
            failureListener = {
                failureListener("카드 등록에 실패하였습니다.")
            }
        )
    }

    /** 카드 리스트 조회 **/
    fun selectCardList(id: String) = client.selectCardList(id)

    /** 대표 카드 업데이트 **/
    suspend fun updateRepresentative(
        cardNumber: String,
        isRepresentative: Boolean,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateRepresentative(
            cardNumber = cardNumber,
            isRepresentative = isRepresentative,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 카드 정보 조회 **/
    suspend fun selectCardInfo(
        cardNumber: String,
        successListener: (CardInfo) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectCardInfo(
            cardNumber = cardNumber,
            successListener = {
                successListener(it.mapper())
            },
            failureListener = failureListener
        )
    }

    /** 카드 이름 변경 **/
    suspend fun updateCardName(
        cardNumber: String,
        cardName: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateCardName(
            cardNumber = cardNumber,
            cardName = cardName,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 카드 충전 **/
    suspend fun updateBalance(
        cardNumber: String,
        balance: Long,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateBalance(
            cardNumber = cardNumber,
            balance = balance,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    /** 카드 삭제 **/
    suspend fun deleteCard(
        cardNumber: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.deleteCard(
            cardNumber = cardNumber,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}