package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UsageHistoryEntity(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "cardNumber") val cardNumber: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "whereToUse") val whereToUse: String,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "amount") val amount: Long,
    @ColumnInfo(name = "detailIndex") val detailIndex: Int,
)