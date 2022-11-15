package com.example.starbucksclone.view.main.pay.card_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val repository: CardRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _cardInfo = mutableStateOf(CardInfo())
    val cardInfo: State<CardInfo> = _cardInfo

    private val _modifyCardName = mutableStateOf("")

    private val _status = MutableStateFlow<Status>(Status.Init)
    val status: StateFlow<Status> = _status

    init {
        savedStateHandle.get<String>("cardNumber")?.let {
            selectCardInfo(it)
        }
    }

    fun event(event: CardDetailEvent) {
        when(event) {
            is CardDetailEvent.ModifyCardName -> {
                _modifyCardName.value = event.name
            }
            is CardDetailEvent.UpdateCardName -> {
                updateCardName(_cardInfo.value.cardNumber, _modifyCardName.value)
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
                _cardInfo.value = CardInfo()
            }
        )
    }

    private fun updateCardName(
        cardNumber: String,
        cardName: String
    ) = viewModelScope.launch {
        repository.updateCardNumber(
            cardNumber = cardNumber,
            cardName = cardName,
            successListener = {
                _status.value = Status.Success

                _cardInfo.value = _cardInfo.value.copy(
                    name = _modifyCardName.value
                )
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