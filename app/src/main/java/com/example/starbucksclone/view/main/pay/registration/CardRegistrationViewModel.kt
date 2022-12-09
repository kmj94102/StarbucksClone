package com.example.starbucksclone.view.main.pay.registration

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
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

    private val _modalState = mutableStateOf(0)
    val modalState: State<Int> = _modalState

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
            is CardRegistrationEvent.ModalChange -> {
                _modalState.value = event.value
            }
            is CardRegistrationEvent.BarcodeRegistration -> {
                barCodeRegistration(event.number)
            }
            is CardRegistrationEvent.CouponRegistration -> {
                couponRegistration()
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
        Log.e("+++++", "cardRegistration")
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

    /** 바코드 카드 추가 **/
    private fun barCodeRegistration(
        number: String
    ) {
        Log.e("+++++", "barCodeRegistration")
        var pinNumber = ""
        val random = Random()
        (0..7).forEach { _ ->
            pinNumber = "$pinNumber${random.nextInt(10)}"
        }

        _cardInfo.value = CardRegistrationInfo(
            cardNumber = number,
            pinNumber = pinNumber
        )
        cardRegistration()
    }

    /** 쿠폰 카드 추가 **/
    private fun couponRegistration() {
        Log.e("+++++", "couponRegistration")
        var cardNumber = ""
        val random = Random()
        (0..15).forEach { _ ->
            cardNumber = "$cardNumber${random.nextInt(10)}"
        }
        var pinNumber = ""
        (0..7).forEach { _ ->
            pinNumber = "$pinNumber${random.nextInt(10)}"
        }
        _cardInfo.value = CardRegistrationInfo(
            cardNumber = cardNumber,
            pinNumber = pinNumber
        )
        cardRegistration()
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