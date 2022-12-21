package com.example.starbucksclone.view.main.order.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var name = ""
    private var indexes = ""

    private val _isHotSelect = mutableStateOf(true)
    val isHotSelect: State<Boolean> = _isHotSelect
    private val _state = mutableStateOf(0)
    val state: State<Int> = _state
    private val _infoList = mutableStateListOf<MenuDetailInfo>()
    val info: State<MenuDetailInfo>
        get() = mutableStateOf(_infoList.getOrNull(_state.value) ?: MenuDetailInfo())


    init {
        savedStateHandle.get<String>("indexes")?.let {
            indexes = it
        }
        savedStateHandle.get<String>("name")?.let {
            name = it
        }
        selectMenuDetail()
    }

    private fun selectMenuDetail() = viewModelScope.launch {
        val result = repository.selectMenuDetails(indexes, name)
        _infoList.addAll(result)
    }

    fun event(event: MenuDetailEvent) {
        when (event) {
            is MenuDetailEvent.HotIcedChange -> {
                _isHotSelect.value = event.isHot
                _state.value = if (event.isHot) 0 else 1
            }
        }
    }

}