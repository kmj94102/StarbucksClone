package com.example.starbucksclone.view.main.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.di.getLoginNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: SharedPreferences
): ViewModel() {
    val id = preferences.getLoginId()
    val nickname = preferences.getLoginNickname() ?: "민재"

}