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

    companion object {
        const val LoginId = "login_id"
        const val Nickname = "nickname"
        const val DatabaseVersion = "database_version"
        const val SearchHistory = "search history"
        const val HistoryClassification = "/|/"
    }
}

fun SharedPreferences.setLoginId(id: String) {
    edit().putString(SharedPreferencesUtil.LoginId, id).apply()
}

fun SharedPreferences.getLoginId() =
    getString(SharedPreferencesUtil.LoginId, null)

fun SharedPreferences.setLoginNickname(id: String) {
    edit().putString(SharedPreferencesUtil.Nickname, id).apply()
}

fun SharedPreferences.getLoginNickname() =
    getString(SharedPreferencesUtil.Nickname, null)

fun SharedPreferences.setDatabaseVersion(type: String, version: Float) =
    edit().putFloat("${type}_${SharedPreferencesUtil.DatabaseVersion}", version).apply()

fun SharedPreferences.getDatabaseVersion(type: String) =
    getFloat("${type}_${SharedPreferencesUtil.DatabaseVersion}", 0.0f)