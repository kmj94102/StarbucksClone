package com.example.starbucksclone.view.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.LoginInfo
import com.example.starbucksclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _id = mutableStateOf("")
    val id: State<String> = _id

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _status = MutableStateFlow<LoginStatus>(LoginStatus.Init)
    val status: StateFlow<LoginStatus> = _status

    fun event(event: LoginEvent) {
        when(event) {
            is LoginEvent.Login -> {
                login()
            }
            is LoginEvent.IdChange -> {
                _id.value = event.id
            }
            is LoginEvent.PasswordChange -> {
                _password.value = event.password
            }
        }
    }

    private fun login() = viewModelScope.launch {
        repository.login(
            loginInfo = LoginInfo(id = _id.value, password = _password.value),
            successListener = {
                _status.value = LoginStatus.Success
            },
            failureListener = {
                _status.value = LoginStatus.Failure
            }
        )

        delay(100)
        _status.value = LoginStatus.Init
    }

    sealed class LoginStatus {
        object Init: LoginStatus()
        object Success: LoginStatus()
        object Failure: LoginStatus()
    }

}