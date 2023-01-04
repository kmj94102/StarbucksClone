package com.example.starbucksclone.view.main.pay.list

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val repository: CardRepository,
    private val pref: SharedPreferences
) : ViewModel() {

    /** 카드 리스트 **/
    private val _cardList = mutableStateListOf<CardInfo>()
    val cardList: List<CardInfo> = _cardList

    init {
        pref.getLoginId()?.let {
            selectCardList(it)
        }
    }

    fun event(event: CardListEvent) {
        when(event) {
            is CardListEvent.UpdateRepresentative -> {
                updateRepresentative(event.cardNumber)
            }
        }
    }

    /** 카드 리스트 조회 **/
    private fun selectCardList(id: String) {
        repository.selectCardList(id = id)
            .onEach { list ->
                _cardList.clear()
                _cardList.addAll(
                    list.map { entity -> entity.mapper(true) }
                )
            }
            .catch {
                _cardList.clear()
            }
            .launchIn(viewModelScope)
    }

    /** 대표카드 업데이트 **/
    private fun updateRepresentative(
        cardNumber: String
    ) = viewModelScope.launch {
        // 이전 대표카드 제거
        repository.updateRepresentative(
            cardNumber = _cardList[0].cardNumber,
            isRepresentative = false,
            successListener = {},
            failureListener = {}
        )
        // 신규 대표카드 설정
        repository.updateRepresentative(
            cardNumber = cardNumber,
            isRepresentative = true,
            successListener = {},
            failureListener = {}
        )
    }

}