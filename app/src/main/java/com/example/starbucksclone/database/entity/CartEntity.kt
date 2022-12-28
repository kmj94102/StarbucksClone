package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "menuIndex") val menuIndex: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nameEng") val nameEng: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "property") val property: String,
    @ColumnInfo(name = "date") val date: Long
)