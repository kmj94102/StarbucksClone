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
) {
    fun mapper() = OrderMenuInfo(
        name = name,
        nameEng = nameEng,
        image = image,
        group = group,
        color = color
    )
}

fun createOrderMenuEntity(item: List<String>): OrderMenuEntity? =
    try {
        OrderMenuEntity(
            index = item[0],
            name = item[1],
            nameEng = item[2],
            group = item[3],
            image = item[4],
            color = item[5]
        )
    } catch (e: Exception) {
        null
    }

data class OrderMenuInfo(
    val name: String,
    val nameEng: String,
    val image: String,
    val group: String,
    val color: String
)
