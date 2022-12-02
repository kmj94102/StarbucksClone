package com.example.starbucksclone.view.login.signup

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.database.entity.SignupInfo
import com.example.starbucksclone.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(

) : ViewModel() {

    private val _step = mutableStateOf(1)
    val step: State<Int> = _step

    private val _isEnable = mutableStateOf(false)
    val isEnable: State<Boolean> = _isEnable

    private val _isIdentificationAgree = mutableStateOf(false)
    val isIdentificationAgree: State<Boolean> = _isIdentificationAgree

    private val _signupInfo = mutableStateOf(SignupInfo())
    val signupInfo: State<SignupInfo> = _signupInfo

    private var _certificationNumber = mutableStateOf("")
    val certificationNumber: State<String> = _certificationNumber

    private var countDownTimer: CountDownTimer? = null
    private val _certificationTime = mutableStateOf("")
    val certificationTime: State<String> = _certificationTime

    fun event(event: SignupEvent) {
        when (event) {
            is SignupEvent.IdentificationAgree -> {
                _isIdentificationAgree.value = event.isAgree
            }
            is SignupEvent.ConditionComplete -> {
                _isEnable.value = event.isComplete
            }
            is SignupEvent.NextStep -> {
                _step.value = _step.value + 1
                _isEnable.value = false
            }
            is SignupEvent.TextChange -> {
                textChange(event.type, event.text)
            }
            is SignupEvent.NewCertificationNumber -> {
                createNewCertificationNumber()
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

    private fun textChange(type: String, text: String) {
        when (type) {
            Name -> {
                _signupInfo.value = _signupInfo.value.copy(name = text)
            }
            Birthday -> {
                _signupInfo.value = _signupInfo.value.copy(birthday = text)
            }
            Phone -> {
                _signupInfo.value = _signupInfo.value.copy(phone = text)
            }
        }
    }

    companion object {
        const val Name = "name"
        const val Birthday = "birthday"
        const val Phone = "phone"
    }

}