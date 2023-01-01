package com.example.starbucksclone.view.main.order.payment

import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.repository.CartRepository
import com.example.starbucksclone.repository.UsageHistoryRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val cardRepository: CardRepository,
    private val usageHistoryRepository: UsageHistoryRepository,
    private val pref: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectCard = mutableStateOf(CardInfo())
    val selectCard: State<CardInfo> = _selectCard

    private val _cardList = mutableStateListOf<CardInfo>()
    val cardList: List<CardInfo> = _cardList

    private val _cartLst = mutableStateListOf<CartEntity>()
    val cartList: List<CartEntity> = _cartLst

    private val id = pref.getLoginId() ?: ""

    private val _modalState = mutableStateOf(0)
    val modalState: State<Int> = _modalState

    private val _status = MutableStateFlow<PaymentStatus>(PaymentStatus.Init)
    val status: StateFlow<PaymentStatus> = _status

    private var isCart = true

    init {
        savedStateHandle.get<String>("item")?.let {
            val item = Gson().fromJson(Uri.decode(it), CartEntity::class.java)
            _cartLst.clear()
            _cartLst.add(item)

            isCart = false
        }
        selectCardList()
        if (isCart) {
            selectCartLit()
        }
    }

    fun event(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.ModalStateChange -> {
                _modalState.value = event.value
            }
            is PaymentEvent.SelectCardChange -> {
                _selectCard.value =
                    _cardList.find { it.cardNumber == event.cardNumber } ?: CardInfo()
            }
            is PaymentEvent.Payment -> payment(totalPrice = event.totalPrice)
        }
    }

    private fun selectCardList() {
        cardRepository.selectCardList(id)
            .onEach {
                _cardList.clear()
                _cardList.addAll(it.map { entity -> entity.mapper() })
                it.find { entity -> entity.representative }?.let { info ->
                    _selectCard.value = info.mapper()
                }
            }
            .catch {
                _cardList.clear()
                _selectCard.value = CardInfo()
            }
            .launchIn(viewModelScope)
    }

    private fun selectCartLit() {
        cartRepository.selectCartItems(id = id)
            .onEach {
                _cartLst.clear()
                _cartLst.addAll(it)
            }
            .catch {
                _cartLst.clear()
            }
            .launchIn(viewModelScope)
    }

    private fun payment(totalPrice: Int) = viewModelScope.launch {
        cardRepository.updateBalance(
            cardNumber = selectCard.value.cardNumber,
            balance = selectCard.value.balance - totalPrice.toLong(),
            successListener = {
                if (isCart) {
                    deleteCartItems()
                } else {
                    insertUsageHistoryList()
                }
            },
            failureListener = {
                _status.value = PaymentStatus.PaymentFailure
            }
        )
    }

    private fun deleteCartItems() = viewModelScope.launch {
        cartRepository.allDeleteCartItems(
            id = id,
            successListener = {
                insertUsageHistoryList()
            },
            failureListener = {
                _status.value = PaymentStatus.PaymentFailure
            }
        )
    }

    private fun insertUsageHistoryList() = viewModelScope.launch {
        usageHistoryRepository.insertUsageHistory(
            list = _cartLst.map { it.historyMapper(selectCard.value.cardNumber) },
            successListener = {
                _status.value = PaymentStatus.PaymentSuccess
            },
            failureListener = {
                _status.value = PaymentStatus.PaymentFailure
            }
        )
    }

    sealed class PaymentStatus {
        object Init : PaymentStatus()
        object PaymentSuccess : PaymentStatus()
        object PaymentFailure : PaymentStatus()
    }

}