package com.example.starbucksclone.view.main.pay.charging

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingViewModel @Inject constructor(
    private val repository: CardRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    /** 카드 정보 **/
    private val _cardInfo = mutableStateOf(CardInfo())
    val cardInfo: State<CardInfo> = _cardInfo

    /** 충전 금액 **/
    private val _chargingAmount = mutableStateOf(0L)
    val chargingAmount: State<Long> = _chargingAmount

    /** 상태 관리 **/
    private val _status = MutableStateFlow<ChargingStatus>(ChargingStatus.Init)
    val status: StateFlow<ChargingStatus> = _status

    /** 카드 번호 **/
    private val cardNumber = savedStateHandle.get<String>(Constants.CardNumber)

    init {
        if (cardNumber == null) {
            _status.value = ChargingStatus.EmptyCardNumber
        } else {
            selectCardInfo(cardNumber)
        }
    }

    fun event(event: ChargingEvent) {
        when(event) {
            is ChargingEvent.ChargingAmountChange -> {
                _chargingAmount.value = event.amount
            }
            is ChargingEvent.CardCharging -> {
                updateBalance()
            }
        }
    }

    /** 카드 정보 조회 **/
    private fun selectCardInfo(cardNumber: String) = viewModelScope.launch {
        repository.selectCardInfo(
            cardNumber = cardNumber,
            successListener = {
                _cardInfo.value = it
            },
            failureListener = {
                _status.value = ChargingStatus.Failure
            }
        )
    }

    /** 잔액 충전 **/
    private fun updateBalance() = viewModelScope.launch {
        repository.updateBalance(
            cardNumber = cardNumber ?: "",
            balance = cardInfo.value.balance + _chargingAmount.value,
            successListener = {
                _status.value = ChargingStatus.Success
            },
            failureListener = {
                _status.value = ChargingStatus.Failure
            }
        )
    }

    sealed class ChargingStatus {
        object Init: ChargingStatus()
        object Success: ChargingStatus()
        object Failure: ChargingStatus()
        object EmptyCardNumber: ChargingStatus()
    }

}