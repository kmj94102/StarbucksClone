package com.example.starbucksclone.view.main.pay.usage_history

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.UsageHistoryInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.UsageHistoryRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UsageHistoryViewModel @Inject constructor(
    private val repository: UsageHistoryRepository,
    private val savedStateHandle: SavedStateHandle,
    private val pref: SharedPreferences
): ViewModel() {

    /** 이용 내역 리스트 **/
    private val _list = mutableStateListOf<UsageHistoryInfo>()
    val list: List<UsageHistoryInfo> = _list

    private val id = pref.getLoginId() ?: ""

    init {
        savedStateHandle.get<String>(Constants.CardNumber)?.let {
            selectUsageHistoryList(it)
        }
    }

    /** 이용 내역 조회 **/
    private fun selectUsageHistoryList(cardNumber: String) {
        repository.selectUsageHistoryList(id = id, cardNumber = cardNumber)
            .onEach {
                _list.clear()
                _list.addAll(it.map { entity -> entity.mapper() })
            }
            .catch {
                _list.clear()
            }
            .launchIn(viewModelScope)
    }

}