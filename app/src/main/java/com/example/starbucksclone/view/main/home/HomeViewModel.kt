package com.example.starbucksclone.view.main.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.HomeNewMenu
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.di.getLoginNickname
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository
): ViewModel() {

    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> = _isLogin

    private val _nickname = mutableStateOf("")
    val nickname: State<String> = _nickname

    private val _newMenuList = mutableStateListOf<HomeNewMenu>()
    val newMenuList: List<HomeNewMenu> = _newMenuList

    init {
        _isLogin.value = pref.getLoginId().isNullOrEmpty().not()
        _nickname.value = pref.getLoginNickname() ?: ""
        selectNewMenuList()
    }

    fun event(event: HomeEvent) {
        when(event) {
            is HomeEvent.Logout -> { logout() }
        }
    }

    private fun logout() {
        userRepository.logout()
        _nickname.value = ""
        _isLogin.value = false
    }

    private fun selectNewMenuList() {
        menuRepository.selectHomeNewMenuList()
            .onEach {
                _newMenuList.clear()
                _newMenuList.addAll(it)
            }
            .catch {
                _newMenuList.clear()
            }
            .launchIn(viewModelScope)
    }

}