package com.example.starbucksclone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starbucksclone.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class StarbucksDatabase: RoomDatabase() {

    abstract fun dao(): StarbucksDao

    companion object {
        const val Database_Name = "starbucks_clone.db"
    }

}