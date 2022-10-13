package com.example.starbucksclone.view.login.sign_up

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.UserTemp
import com.example.starbucksclone.repository.UserRepository
import com.example.starbucksclone.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentState = mutableStateOf(0)
    val currentState: State<Int> = _currentState

    private var _certificationNumber = mutableStateOf("")
    val certificationNumber: State<String> = _certificationNumber

    private var countDownTimer: CountDownTimer? = null
    private val _certificationTime = mutableStateOf("")
    val certificationTime: State<String> = _certificationTime

    private val _eventStatus = MutableStateFlow<Event>(Event.Init)
    val eventStatus: StateFlow<Event> = _eventStatus

    private val _userInfo = mutableStateOf(UserTemp())

    init {
        savedStateHandle.get<Boolean>("isPush")?.let {
            _userInfo.value = _userInfo.value.copy(pushConsent = it)
        }
    }

    fun event(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.NextState -> {
                _currentState.value = _currentState.value + 1
            }
            is SignUpEvent.NewCertificationNumber -> {
                createNewCertificationNumber()
                event.newNumber(certificationNumber.value)
            }
            is SignUpEvent.SelfAuthenticationResult -> {
                _userInfo.value = _userInfo.value.copy(
                    name = event.name,
                    birthday = event.birthday,
                    phone = event.phone
                )
                _currentState.value = 1
            }
            is SignUpEvent.IdPasswordResult -> {
                _userInfo.value = _userInfo.value.copy(
                    id = event.id,
                    password = event.password
                )
                _currentState.value = 2
            }
            is SignUpEvent.EmailResult -> {
                _userInfo.value = _userInfo.value.copy(
                    email = event.email,
                )
                _currentState.value = 3
            }
            is SignUpEvent.NicknameResult -> {
                _userInfo.value = _userInfo.value.copy(
                    nickname = event.nickname,
                )
                event(SignUpEvent.Complete)
            }
            is SignUpEvent.Complete -> {
                insertUser()
            }
        }
    }

    private fun createNewCertificationNumber() {
        var certificationNumber = ""
        val random = Random()
        (0..5).forEach { _ ->
            certificationNumber = "$certificationNumber${random.nextInt(10)}"
        }
        _certificationNumber.value = certificationNumber
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(180000L, 1000) {
            override fun onTick(millisRemaining: Long) {
                _certificationTime.value = millisRemaining.formatTime()
            }

            override fun onFinish() {
                countDownTimer?.cancel()
            }
        }.start()
    }

    private fun insertUser() = viewModelScope.launch {
        repository.insertUser(
            user = _userInfo.value.mapper(),
            successListener = {
                _eventStatus.value = Event.Success
            },
            failureListener = {
                _eventStatus.value = Event.Failure
            }
        )
    }

    sealed class Event {
        object Init: Event()
        object Success: Event()
        object Failure: Event()
    }

}