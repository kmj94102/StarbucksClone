package com.example.starbucksclone.view.main.pay.list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardListInfo
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val repository: CardRepository
): ViewModel() {

    private val _cardList = mutableStateListOf<CardListInfo>()
    val cardList: List<CardListInfo> = _cardList

    init {
        selectCardList()
    }

    private fun selectCardList() {
        _cardList.clear()
        repository.selectCardList()
            .onEach {
                _cardList.addAll(
                    it.map { entity ->
                        CardListInfo(
                            cardNumber = entity.cardNumber,
                            name = "${entity.cardName}(${entity.cardNumber.substring(10, entity.cardNumber.length)})",
                            image = entity.cardImage,
                            balance = entity.balance
                        )
                    }
                )
            }
            .catch { _cardList.clear() }
            .launchIn(viewModelScope)
    }

}