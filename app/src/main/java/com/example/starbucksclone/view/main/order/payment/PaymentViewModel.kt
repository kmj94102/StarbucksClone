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
import com.example.starbucksclone.database.entity.PaymentInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.repository.CartRepository
import com.example.starbucksclone.repository.UsageHistoryRepository
import com.example.starbucksclone.util.Constants
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

    /** 선택한 카드 정보 **/
    private val _selectCard = mutableStateOf(CardInfo())
    val selectCard: State<CardInfo> = _selectCard

    /** 카드 리스트 **/
    private val _cardList = mutableStateListOf<CardInfo>()
    val cardList: List<CardInfo> = _cardList

    /** 결제 정보 리스트 **/
    private val _paymentList = mutableStateListOf<PaymentInfo>()
    val paymentList: List<PaymentInfo> = _paymentList

    private val id = pref.getLoginId() ?: ""

    /** 바텀 다이얼로그 상태 **/
    private val _modalState = mutableStateOf(0)
    val modalState: State<Int> = _modalState

    /** 상태 관리 **/
    private val _status = MutableStateFlow<PaymentStatus>(PaymentStatus.Init)
    val status: StateFlow<PaymentStatus> = _status

    /** 장바구니/단건 결제 여부 **/
    private var isCart = true

    init {
        savedStateHandle.get<String>(Constants.Item)?.let {
            val item = Gson().fromJson(Uri.decode(it), PaymentInfo::class.java)
            _paymentList.clear()
            _paymentList.add(item)

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

    /** 카드 리스트 조회 **/
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

    /** 장바구니 조회 **/
    private fun selectCartLit() = viewModelScope.launch {
        cartRepository.selectCartItems(
            id = id,
            successListener = {
                _paymentList.clear()
                _paymentList.addAll(it.map { entity -> entity.paymentInfoMapper() })
            },
            failureListener = {
                _paymentList.clear()
            }
        )
    }

    /** 결제하기 **/
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

    /** 장바구니 아이템 삭제 **/
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

    /** 이용내역 추가 **/
    private fun insertUsageHistoryList() = viewModelScope.launch {
        usageHistoryRepository.insertUsageHistory(
            list = _paymentList.map { it.historyMapper(selectCard.value.cardNumber) },
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