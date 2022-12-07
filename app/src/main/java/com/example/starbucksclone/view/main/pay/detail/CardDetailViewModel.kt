package com.example.starbucksclone.view.main.pay.detail

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
) : ViewModel() {

    private val _cardInfo = mutableStateOf(CardInfo())
    val cardInfo: State<CardInfo> = _cardInfo

    private val _modifyCardName = mutableStateOf("")
    val modifyCardName: State<String> = _modifyCardName

    private val _status = MutableStateFlow<CardDetailStatus>(CardDetailStatus.Init)
    val status: StateFlow<CardDetailStatus> = _status

    init {
        savedStateHandle.get<String>("cardNumber")?.let {
            selectCardInfo(it)
        }
    }

    fun event(event: CardDetailEvent) {
        when (event) {
            is CardDetailEvent.CardNameChange -> {
                _modifyCardName.value = event.cardName
            }
            is CardDetailEvent.InitCardNameModify -> {
                _modifyCardName.value = _cardInfo.value.name
            }
            is CardDetailEvent.CardNameModify -> {
                updateCardName()
            }
            is CardDetailEvent.CardDelete -> {
                deleteCard()
            }
        }
    }

    /** 카드 정보 조회 **/
    private fun selectCardInfo(
        cardNumber: String
    ) = viewModelScope.launch {
        repository.selectCardInfo(
            cardNumber = cardNumber,
            successListener = {
                _cardInfo.value = it
                _modifyCardName.value = it.name
            },
            failureListener = {
                _status.value = CardDetailStatus.Error("카드 조회를 실패하였습니다.")
            }
        )
    }

    /** 카드 이름 업데이트 **/
    private fun updateCardName() = viewModelScope.launch {
        repository.updateCardNumber(
            cardNumber = _cardInfo.value.cardNumber,
            cardName = _modifyCardName.value,
            successListener = {
                _cardInfo.value = _cardInfo.value.copy(name = _modifyCardName.value)
            },
            failureListener = {
                _status.value = CardDetailStatus.Error("카드 이름 변경을 실패하였습니다.")
            }
        )
    }

    /** 카드 삭제 **/
    private fun deleteCard() = viewModelScope.launch {
        repository.deleteCard(
            cardNumber = _cardInfo.value.cardNumber,
            successListener = {
                _status.value = CardDetailStatus.CardDeleteSuccess
            },
            failureListener = {
                _status.value = CardDetailStatus.Error("카드 삭제에 실패하였습니다.")
            }
        )
    }

    sealed class CardDetailStatus {
        object Init : CardDetailStatus()
        object CardDeleteSuccess: CardDetailStatus()
        data class Error(val msg: String) : CardDetailStatus()
    }

}