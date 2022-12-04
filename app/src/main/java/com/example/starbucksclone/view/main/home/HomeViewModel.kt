package com.example.starbucksclone.view.main.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.di.getLoginNickname
import com.example.starbucksclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repository: UserRepository
): ViewModel() {

    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> = _isLogin

    private val _nickname = mutableStateOf("")
    val nickname: State<String> = _nickname

    init {
        _isLogin.value = pref.getLoginId().isNullOrEmpty().not()
        _nickname.value = pref.getLoginNickname() ?: ""
    }

    fun event(event: HomeEvent) {
        when(event) {
            is HomeEvent.Logout -> { logout() }
        }
    }

    private fun logout() {
        repository.logout()
        _nickname.value = ""
        _isLogin.value = false
    }

}