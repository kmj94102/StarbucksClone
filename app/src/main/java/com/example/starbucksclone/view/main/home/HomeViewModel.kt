package com.example.starbucksclone.view.main.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.util.getLoginId
import com.example.starbucksclone.util.getLoginNickname
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: SharedPreferences
): ViewModel() {
    val id = preferences.getLoginId()
    val nickname = preferences.getLoginNickname() ?: "민재"

}