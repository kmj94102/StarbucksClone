package com.example.starbucksclone.view.main.order.search

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuSearchViewModel @Inject constructor(
    private val pref: SharedPreferences
) : ViewModel() {

    private val _historyList = mutableStateListOf<String>()
    val historyList: List<String> = _historyList

    init {
        pref.getString(Constants.SearchHistory, null)
            ?.split(Constants.HistoryClassification)
            ?.let {
                _historyList.addAll(it)
            }
        _historyList.addAll(
            listOf("돌체", "검색", "했다")
        )
    }

}