package com.example.starbucksclone.view.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.LoginInfo
import com.example.starbucksclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _loginInfo = mutableStateOf(LoginInfo())
    val loginInfo: State<LoginInfo> = _loginInfo

    private val _status = MutableStateFlow<Status>(Status.Init)
    val status: StateFlow<Status> = _status

    fun event(event: LoginEvent) {
        when(event) {
            is LoginEvent.IdChange -> {
                _loginInfo.value = _loginInfo.value.copy(id = event.id)
            }
            is LoginEvent.PwChange -> {
                _loginInfo.value = _loginInfo.value.copy(password = event.pw)
            }
            is LoginEvent.Login -> {
                login()
            }
        }
    }

    /** 로그인 **/
    private fun login() = viewModelScope.launch {
        repository.login(
            loginInfo = loginInfo.value,
            successListener = {
                _status.value = Status.Success
            },
            failureListener = {
                _status.value = Status.Failure
            }
        )
    }

    sealed class Status {
        object Init: Status()
        object Success: Status()
        object Failure: Status()
    }

}