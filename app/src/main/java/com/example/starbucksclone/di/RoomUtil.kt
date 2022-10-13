package com.example.starbucksclone.di

import android.content.Context
import androidx.room.Room
import com.example.starbucksclone.database.StarbucksDao
import com.example.starbucksclone.database.StarbucksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomUtil {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StarbucksDatabase =
        Room.databaseBuilder(context, StarbucksDatabase::class.java, StarbucksDatabase.Database_Name)
            .build()

    @Provides
    @Singleton
    fun providePokemonDao(
        database: StarbucksDatabase
    ): StarbucksDao =
        database.dao()

}