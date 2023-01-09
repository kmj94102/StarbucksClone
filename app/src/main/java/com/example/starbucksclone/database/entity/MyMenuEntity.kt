package com.example.starbucksclone.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyMenuEntity(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "menuIndex") val menuIndex: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nameEng") val nameEng: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "anotherName") val anotherName: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "property") val property: String,
    @ColumnInfo(name = "date") val date: Long
) {
    fun mapper() = MyMenuInfo(
        index = index,
        menuIndex = menuIndex,
        image = image,
        name = name,
        nameEng = nameEng,
        anotherName = anotherName,
        price = price,
        property = property
    )
}

data class MyMenuInfo(
    val index: Int,
    val menuIndex: Int,
    val image: String,
    val name: String,
    val nameEng: String,
    val anotherName: String,
    val price: Int,
    val property: String
)