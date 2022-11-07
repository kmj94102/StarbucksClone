package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MenuDetailEntity(
    @PrimaryKey val index: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "nameEng") val nameEng: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "image") val image: String = "",
    @ColumnInfo(name = "type") val type: String = ""
)

fun createDrinkDetailEntity(item: List<String>): MenuDetailEntity? =
    try {
        MenuDetailEntity(
            index = item[0],
            name = item[1],
            nameEng = item[2],
            description = item[3],
            image = item[4],
            type = item[5]
        )
    } catch (e: Exception) {
        null
    }