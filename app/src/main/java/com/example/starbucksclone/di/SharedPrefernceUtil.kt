package com.example.starbucksclone.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesUtil {
    @Provides
    @Singleton
    fun provedSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("starbucks_clone", Context.MODE_PRIVATE)
}