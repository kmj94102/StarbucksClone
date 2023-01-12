package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MenuEntity(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "nameEng") val nameEng: String,
    @ColumnInfo(name = "indexes") val indexes: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "size") val size: String,
    @ColumnInfo(name = "sizePrice") val sizePrice: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "isBest") val isBest: Boolean,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    @ColumnInfo(name = "isRecommendation") val isRecommendation: Boolean,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "orderGroup") val orderGroup: String
) {
    fun mapper() = MenuInfo(
        indexes = indexes,
        name = name,
        nameEng = nameEng,
        image = image,
        price = price,
        group = orderGroup
    )
}

fun createDrinkEntity(item: List<String>): MenuEntity? =
    try {
        MenuEntity(
            indexes = item[0],
            name = item[1],
            nameEng = item[2],
            image = item[3],
            price = item[4].toIntOrNull() ?: 0,
            size = item[5],
            sizePrice = item[6],
            type = item[7],
            color = item[8],
            isBest = item[9] == "true",
            isNew = item[10] == "true",
            isRecommendation = item[11] == "true",
            group = item[12],
            orderGroup = item[13]
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

data class MenuSearchResult(
    val name: String,
    val nameEng: String,
    val indexes: String,
    val image: String,
    val price: Int,
    val color: String,
    val isBest: Boolean,
    val group: String
)

data class HomeNewMenu(
    val indexes: String,
    val name: String,
    val image: String,
    val group: String
)

data class MenuInfo(
    val indexes: String,
    val image: String,
    val name: String,
    val nameEng: String,
    val price: Int,
    val group: String
)