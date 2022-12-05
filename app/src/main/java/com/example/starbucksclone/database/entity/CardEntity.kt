package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardEntity(
    @PrimaryKey val cardNumber: String,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cardName") val cardName: String,
    @ColumnInfo(name = "pinNumber") val pinNumber: String,
    @ColumnInfo(name = "cardImage") val cardImage: String,
    @ColumnInfo(name = "balance") val balance: Long,
    @ColumnInfo(name = "representative") val representative: Boolean,
) {
    fun mapper() = CardInfo(
        cardNumber = cardNumber,
        name = cardName,
        balance = balance,
        image = cardImage
    )
}

data class CardRegistrationInfo(
    val cardName: String = "",
    val cardNumber: String = "",
    val pinNumber: String = ""
)

data class CardInfo(
    val cardNumber: String = "",
    val name: String = "",
    val balance: Long = 0,
    val image: String = ""
)