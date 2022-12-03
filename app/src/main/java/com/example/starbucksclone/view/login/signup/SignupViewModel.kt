package com.example.starbucksclone.view.login.signup

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.SignupInfo
import com.example.starbucksclone.repository.UserRepository
import com.example.starbucksclone.util.formatTime
import com.example.starbucksclone.util.isEmailFormat
import com.example.starbucksclone.util.isKoreanFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //회원가입 진행 단계
    private val _step = mutableStateOf(1)
    val step: State<Int> = _step

    // 다음 버튼 활성화 여부
    private val _isEnable = mutableStateOf(false)
    val isEnable: State<Boolean> = _isEnable

    // 본인인증 약관 동의 여부
    private val _isIdentificationAgree = mutableStateOf(false)
    val isIdentificationAgree: State<Boolean> = _isIdentificationAgree

    // 회원가입 정보
    private val _signupInfo = mutableStateOf(SignupInfo())
    val signupInfo: State<SignupInfo> = _signupInfo

    // 인증번호
    private var _certificationNumber = mutableStateOf("")
    val certificationNumber: State<String> = _certificationNumber

    // 사용자 입력 인증번호
    private var _userCertificationNumber = mutableStateOf("")
    val userCertificationNumber: State<String> = _userCertificationNumber

    // 인증번호 타이머
    private var countDownTimer: CountDownTimer? = null
    private val _certificationTime = mutableStateOf("")
    val certificationTime: State<String> = _certificationTime

    // 비밀번호 확인
    private var _passwordCheck = mutableStateOf("")
    val passwordCheck: State<String> = _passwordCheck

    // 상태관리
    private val _status = MutableStateFlow<SignupStatus>(SignupStatus.Init)
    val status: StateFlow<SignupStatus> = _status

    init {
        savedStateHandle.get<Boolean>("isPushConsent")?.let {
            _signupInfo.value = _signupInfo.value.copy(
                pushConsent = it,
                eventConsent = it
            )
        }
    }

    fun event(event: SignupEvent) {
        when (event) {
            is SignupEvent.IdentificationAgree -> {
                _isIdentificationAgree.value = event.isAgree
                identificationCheck()
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
                if (_signupInfo.value.phone.length in 10..11) {
                    createNewCertificationNumber()
                    _status.value = SignupStatus.CertificationNumber(_certificationNumber.value)
                } else {
                    _status.value = SignupStatus.Error("휴대폰 번호를 확인해주세요.")
                }

                viewModelScope.launch {
                    delay(100)
                    _status.value = SignupStatus.Init
                }
            }
            is SignupEvent.Signup -> {
                signup()
            }
        }
    }

    /** 인증번호 생성 **/
    private fun createNewCertificationNumber() {
        var certificationNumber = ""
        val random = Random()
        (0..5).forEach { _ ->
            certificationNumber = "$certificationNumber${random.nextInt(10)}"
        }
        _certificationNumber.value = certificationNumber
        startTimer()
    }

    /** 인증번호 타이머 실행 **/
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

    /** 각종 TextField 변경 반영 **/
    private fun textChange(type: String, text: String) {
        when (type) {
            Name -> {
                _signupInfo.value = _signupInfo.value.copy(name = text)
                identificationCheck()
            }
            Birthday -> {
                _signupInfo.value = _signupInfo.value.copy(birthday = text)
                identificationCheck()
            }
            Phone -> {
                _signupInfo.value = _signupInfo.value.copy(phone = text)
                identificationCheck()
            }
            CertificationNumber -> {
                _userCertificationNumber.value = text
                identificationCheck()
            }
            Id -> {
                _signupInfo.value = _signupInfo.value.copy(id = text)
                idPasswordCheck()
            }
            Password -> {
                _signupInfo.value = _signupInfo.value.copy(password = text)
                idPasswordCheck()
            }
            PasswordCheck -> {
                _passwordCheck.value = text
                idPasswordCheck()
            }
            Email -> {
                _signupInfo.value = _signupInfo.value.copy(email = text)
                emailCheck()
            }
            Nickname -> {
                _signupInfo.value = _signupInfo.value.copy(nickname = text)
                nicknameCheck()
            }
        }
    }

    /** 본인인증 조건 확인 **/
    private fun identificationCheck() {
        val info = _signupInfo.value
        val check = _isIdentificationAgree.value && info.name.isNotEmpty() &&
                info.birthday.length == 6 && info.phone.length in 10..11 &&
                _userCertificationNumber.value.length == 6

        _isEnable.value = check
    }

    /** 아이디 패스워드 조건 체크 **/
    private fun idPasswordCheck() {
        val info = _signupInfo.value
        val check =
            info.id.length in 4..13 && info.password.length in 10..20 && info.password == _passwordCheck.value

        _isEnable.value = check
    }

    /** 이메일 조건 체크 **/
    private fun emailCheck() {
        val info = _signupInfo.value
        val check = info.email.isEmailFormat() && info.email.length > 7

        _isEnable.value = check
    }

    /** 닉네임 체크 **/
    private fun nicknameCheck() {
        val info = _signupInfo.value
        val check = info.nickname.isKoreanFormat() && info.nickname.length <= 6

        _isEnable.value = check
    }

    /** 회원가입 **/
    private fun signup() = viewModelScope.launch {
        repository.insertUser(
            user = signupInfo.value.mapper(),
            successListener = {
                _status.value = SignupStatus.SignupComplete
            },
            failureListener = {
                _status.value = SignupStatus.Error("회원가입에 실패하였습니다.")
            }
        )
    }

    companion object {
        const val Name = "name"
        const val Birthday = "birthday"
        const val Phone = "phone"
        const val CertificationNumber = "certificationNumber"
        const val Id = "id"
        const val Password = "password"
        const val PasswordCheck = "password check"
        const val Email = "email"
        const val Nickname = "nickname"
    }

    sealed class SignupStatus {
        object Init : SignupStatus()
        data class CertificationNumber(val number: String) : SignupStatus()
        data class Error(val message: String) : SignupStatus()
        object SignupComplete: SignupStatus()
    }

}