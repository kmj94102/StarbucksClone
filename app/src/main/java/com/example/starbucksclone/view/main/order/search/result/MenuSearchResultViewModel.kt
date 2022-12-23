package com.example.starbucksclone.view.main.order.search.result

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuSearchResult
import com.example.starbucksclone.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSearchResultViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _selected = mutableStateOf("전체")
    val selected: State<String> = _selected

    private val _list = mutableStateListOf<MenuSearchResult>()
    val list: List<MenuSearchResult> = _list

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    init {
        savedStateHandle.get<String>("value")?.let {
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