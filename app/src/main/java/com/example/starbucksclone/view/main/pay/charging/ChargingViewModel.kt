package com.example.starbucksclone.view.main.pay.charging

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingViewModel @Inject constructor(
    private val repository: CardRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _cardInfo = mutableStateOf(CardInfo())
    val cardInfo: State<CardInfo> = _cardInfo

    private val _chargingAmount = mutableStateOf(0L)
    val chargingAmount: State<Long> = _chargingAmount

    private val _status = MutableStateFlow<ChargingStatus>(ChargingStatus.Init)
    val status: StateFlow<ChargingStatus> = _status

    private val cardNumber = savedStateHandle.get<String>("cardNumber")

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