package com.example.starbucksclone.view.main.order.search.result

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuSearchResult
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSearchResultViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    /** 텝 선택 관리 **/
    private val _selected = mutableStateOf("전체")
    val selected: State<String> = _selected

    /** 검색 결과 리스트 **/
    private val _list = mutableStateListOf<MenuSearchResult>()
    val list: List<MenuSearchResult> = _list

    /** 검색어 **/
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    init {
        savedStateHandle.get<String>(Constants.Value)?.let {
            selectSearchMenuList(it)
            _title.value = it
        }
    }

    fun event(event: MenuSearchResultEvent) {
        when(event) {
            is MenuSearchResultEvent.SelectedChange -> {
                _selected.value = event.value
                selectSearchMenuList(_title.value)
            }
        }
    }

    /** 검색 결과 리스트 조회 **/
    private fun selectSearchMenuList(
        value: String
    ) = viewModelScope.launch {
        repository.selectMenuSearchList(
            group = _selected.value,
            name = value,
            successListener = {
                _list.clear()
                _list.addAll(it)
            },
            failureListener = {
                _list.clear()
            }
        )
    }

}