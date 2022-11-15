package com.example.starbucksclone.view.main.pay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    var cardList: List<CardInfo> = listOf()
        private set

    init {
        selectCardList()
    }

    private fun selectCardList() {
        repository.selectCardList()
            .onEach {
                cardList = it.map { entity ->
                    CardInfo(
                        cardNumber = entity.cardNumber,
                        name = "${entity.cardName}(${
                            entity.cardNumber.substring(
                                10,
                                entity.cardNumber.length
                            )
                        })",
                        image = entity.cardImage,
                        balance = entity.balance
                    )
                }
            }
            .catch {  }
            .launchIn(viewModelScope)
    }

}