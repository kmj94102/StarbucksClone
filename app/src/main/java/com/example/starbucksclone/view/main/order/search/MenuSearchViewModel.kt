package com.example.starbucksclone.view.main.order.search

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.di.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuSearchViewModel @Inject constructor(
    private val pref: SharedPreferences
) : ViewModel() {

    /** 검색 기록 리스트 **/
    private val _historyList = mutableStateListOf<String>()
    val historyList: List<String> = _historyList

    init {
        getSearchHistory()
    }

    fun event(event: MenuSearchEvent) {
        when (event) {
            is MenuSearchEvent.Search -> {
                _historyList.remove(event.value)
                _historyList.add(index = 0, element = event.value)
                setSearchHistory()
            }
            is MenuSearchEvent.DeleteHistory -> {
                _historyList.remove(event.value)
                setSearchHistory()
            }
            is MenuSearchEvent.AllDelete -> {
                _historyList.clear()
                allDeleteSearchHistory()
            }
        }
    }

    /** 검색 기록 조회 **/
    private fun getSearchHistory() {
        pref.getString(SharedPreferencesUtil.SearchHistory, null)
            ?.split(SharedPreferencesUtil.HistoryClassification)
            ?.let {
                _historyList.addAll(it)
            }
    }

    /** 검색 기록 등록 **/
    private fun setSearchHistory() {
        pref.edit()
            .putString(
                SharedPreferencesUtil.SearchHistory,
                _historyList.reduce { acc, s ->
                    "$acc${SharedPreferencesUtil.HistoryClassification}$s"
                }
            )
            .apply()
    }

    /** 검색 기록 전체 삭제 **/
    private fun allDeleteSearchHistory() {
        pref.edit()
            .putString(SharedPreferencesUtil.SearchHistory, "")
            .apply()
    }

}