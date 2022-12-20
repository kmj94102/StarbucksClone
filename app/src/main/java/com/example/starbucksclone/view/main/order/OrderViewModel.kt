package com.example.starbucksclone.view.main.order

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.repository.OrderMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderMenuRepository
): ViewModel() {

    private val _list = mutableStateListOf<OrderMenuEntity>()
    val list: List<OrderMenuEntity> = _list

    private val _selectState = mutableStateOf("음료")
    val selectState: State<String> = _selectState

    init {
        selectOderMenu()
    }

    fun event(event: OrderEvent) {
        when(event) {
            is OrderEvent.SelectChange -> {
                _selectState.value = event.select
                selectOderMenu()
            }
        }
    }

    private fun selectOderMenu() {
        repository.selectOrderMenu(_selectState.value)
            .onEach {
                _list.clear()
                _list.addAll(it)
            }
            .catch {
                _list.clear()
            }
            .launchIn(viewModelScope)
    }

}