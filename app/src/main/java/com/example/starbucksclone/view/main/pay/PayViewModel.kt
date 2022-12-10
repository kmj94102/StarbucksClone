package com.example.starbucksclone.view.main.pay

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val repository: CardRepository,
    private val pref: SharedPreferences
): ViewModel() {

    private val _cardList = mutableStateListOf<CardEntity>()
    val cardList: List<CardEntity> = _cardList

    init {
        pref.getLoginId()?.let {
            selectCardList(it)
        }
    }

    private fun selectCardList(id: String) {
        repository.selectCardList(id)
            .onEach {
                _cardList.clear()
                _cardList.addAll(it)
            }
            .catch { _cardList.clear() }
            .launchIn(viewModelScope)
    }

}