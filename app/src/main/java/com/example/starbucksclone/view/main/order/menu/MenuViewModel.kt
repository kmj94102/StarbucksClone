package com.example.starbucksclone.view.main.order.menu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var group = mutableStateOf("")
    var name: State<String> = mutableStateOf("")
        private set
    private val _list = mutableStateListOf<MenuEntity>()
    val list: List<MenuEntity> = _list

    init {
        savedStateHandle.get<String>("name")?.let {
            name = mutableStateOf(it)
        }
        savedStateHandle.get<String>("group")?.let {
            group = mutableStateOf(it)
        }
        selectDrinks()
    }

    private fun selectDrinks() = viewModelScope.launch {
        _list.clear()

        repository.selectDrinks(name.value)
            .onEach { _list.addAll(it) }
            .catch { _list.clear() }
            .launchIn(viewModelScope)
    }

}