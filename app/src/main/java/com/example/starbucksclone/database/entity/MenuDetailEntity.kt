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

data class MenuDetailInfoResult(
    val index: Int,
    val name: String,
    val nameEng: String,
    val description: String,
    val image: String,
    val type: String,
    val size: String,
    val sizePrice: String,
    val color: String,
    val drinkType: String,
    val isBest: Boolean
) {
    fun mapper() = MenuDetailInfo(
        index = index,
        name = name,
        nameEng = nameEng,
        description = description,
        image = image,
        type = type,
        sizes = size.split(", "),
        sizePrices = sizePrice.split(",").map { it.toLong() },
        color = color,
        drinkType = drinkType,
        isBest = isBest
    )
}

data class MenuDetailInfo(
    val index: Int = 0,
    val name: String = "",
    val nameEng: String = "",
    val description: String = "",
    val image: String = "",
    val type: String = "",
    val sizes: List<String> = listOf(),
    val sizePrices: List<Long> = listOf(),
    val color: String = "000000",
    val drinkType: String = "",
    val isBest: Boolean = false
)