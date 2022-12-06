package com.example.starbucksclone.view.main.pay.registration

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardRegistrationViewModel @Inject constructor(
    private val repository: CardRepository,
    private val pref: SharedPreferences
) : ViewModel() {
    /** 카드 등록 정보 **/
    private val _cardInfo = mutableStateOf(CardRegistrationInfo())
    val cardInfo: State<CardRegistrationInfo> = _cardInfo

    /** 카드 이용약관 동의 여부 **/
    private val _isTermsCheck = mutableStateOf(false)
    val isTermsCheck: State<Boolean> = _isTermsCheck

    /** 버트 활성화 여부 **/
    private val _isEnable = mutableStateOf(false)
    val isEnable: State<Boolean> = _isEnable

    /** 로그인한 유저 id **/
    var id: String = pref.getLoginId() ?: ""

    /** 진행 상황 **/
    private val _status = MutableStateFlow<CardRegistrationStatus>(CardRegistrationStatus.Init)
    val status: StateFlow<CardRegistrationStatus> = _status

    fun event(event: CardRegistrationEvent) {
        when (event) {
            is CardRegistrationEvent.TextChange -> {
                textChanged(event)
            }
            is CardRegistrationEvent.TermsCheck -> {
                _isTermsCheck.value = _isTermsCheck.value.not()
                checkEnable()
            }
            is CardRegistrationEvent.CardRegistration -> {
                cardRegistration()
            }
        }
    }

    /** 텍스트 변경 **/
    private fun textChanged(event: CardRegistrationEvent.TextChange) {
        when (event.type) {
            CardName -> {
                _cardInfo.value = _cardInfo.value.copy(cardName = event.text)
            }
            CardNumber -> {
                _cardInfo.value = _cardInfo.value.copy(cardNumber = event.text)
            }
            PinNumber -> {
                _cardInfo.value = _cardInfo.value.copy(pinNumber = event.text)
            }
        }
        checkEnable()
    }

    /** 버튼 활성화 체크 **/
    private fun checkEnable() {
        _isEnable.value = _isTermsCheck.value && cardInfo.value.pinNumber.length == 8 &&
                cardInfo.value.cardNumber.length == 16
    }

    /** 카드 추가 **/
    private fun cardRegistration() = viewModelScope.launch {
        repository.createCard(
            id = id,
            info = _cardInfo.value,
            successListener = {
                _status.value = CardRegistrationStatus.Success
            },
            failureListener = {
                _status.value = CardRegistrationStatus.Failure(it)
            }
        )
        delay(100)
        _status.value = CardRegistrationStatus.Init
    }

    sealed class CardRegistrationStatus {
        object Init : CardRegistrationStatus()
        object Success : CardRegistrationStatus()
        data class Failure(
            val msg: String
        ) : CardRegistrationStatus()
    }

    companion object {
        const val CardName = "card name"
        const val CardNumber = "card number"
        const val PinNumber = "pin number"
    }

}