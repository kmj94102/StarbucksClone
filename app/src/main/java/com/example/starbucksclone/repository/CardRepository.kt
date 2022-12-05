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

    suspend fun createCard(
        id: String,
        info: CardRegistrationInfo,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertCard(
            cardEntity = CardEntity(
                id = id,
                cardName = info.cardName,
                cardNumber = info.cardNumber,
                cardImage = getStarbucksCardImage(),
                pinNumber = info.pinNumber,
                balance = 0,
                representative = false
            ),
            successListener = successListener,
            failureListener = failureListener
        )
    }

    fun selectCardList(id: String) = client.selectCardList(id)

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

    suspend fun updateCardNumber(
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

}