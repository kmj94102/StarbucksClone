package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UsageHistoryEntity(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cardNumber") val cardNumber: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "whereToUse") val whereToUse: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "name") val name: String
)