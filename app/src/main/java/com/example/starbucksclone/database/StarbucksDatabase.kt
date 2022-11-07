package com.example.starbucksclone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.database.entity.UserEntity
import com.example.starbucksclone.util.Constants

@Database(
    entities = [
        UserEntity::class,
        OrderMenuEntity::class,
        MenuEntity::class,
        MenuDetailEntity::class
    ],
    version = 1
)
abstract class StarbucksDatabase: RoomDatabase() {

    abstract fun dao(): StarbucksDao

    companion object {
        const val DatabaseName = "starbucks_clone.db"
        fun currentVersion(type: String): Float {
            return when(type) {
                Constants.Order -> 1.0f
                Constants.Drink -> 1.0f
                Constants.DrinkDetail -> 1.0f
                else -> 1.0f
            }
        }
    }

}