package com.example.starbucksclone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        OrderMenuEntity::class
    ],
    version = 1
)
abstract class StarbucksDatabase: RoomDatabase() {

    abstract fun dao(): StarbucksDao

    companion object {
        const val DatabaseName = "starbucks_clone.db"
        const val CurrentVersion = 1.0f
    }

}