package com.example.starbucksclone.view.login.signup.complete

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupCompleteViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repository: UserRepository
) : ViewModel() {

    /** 상태 관리 **/
    private val _status = MutableStateFlow<SignupCompleteStatus>(SignupCompleteStatus.Init)
    val status: StateFlow<SignupCompleteStatus> = _status

    /** 이름 **/
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    /** 수신 동의 여부 **/
    private val _isPushConsent = mutableStateOf(false)
    val isPushConsent: State<Boolean> = _isPushConsent

    init {
        val id = pref.getLoginId()
        if (id != null) {
            selectSignupCompleteInfo(id)
        } else {
            _status.value = SignupCompleteStatus.Error
        }
    }

    /** 회원가입 정보 조회 **/
    private fun selectSignupCompleteInfo(id: String) = viewModelScope.launch {
        repository.selectSignupCompleteInfo(
            id = id,
            successListener = { name, isPushConsent ->
                _name.value = name
                _isPushConsent.value = isPushConsent
            },
            failureListener = {
                _status.value = SignupCompleteStatus.Error
            }
        )
    }

    sealed class SignupCompleteStatus {
        object Init: SignupCompleteStatus()
        object Error: SignupCompleteStatus()
    }

}