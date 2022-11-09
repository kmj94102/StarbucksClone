package com.example.starbucksclone.repository

import com.example.starbucksclone.database.client.CardClient
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.util.getStarbucksCardImage
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val client: CardClient
) {

    suspend fun createCard(
        info: CardRegistrationInfo,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertCard(
            cardEntity = CardEntity(
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

}