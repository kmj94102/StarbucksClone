package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderMenuEntity(
    @PrimaryKey val index: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nameEng") val nameEng: String,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "color") val color: String
)
