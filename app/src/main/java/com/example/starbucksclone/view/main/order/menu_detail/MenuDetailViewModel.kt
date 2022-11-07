package com.example.starbucksclone.view.main.order.menu_detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _list = mutableStateListOf<MenuDetailEntity>()
    val color: String
    val list: List<MenuDetailEntity> = _list

    var item = mutableStateOf(MenuDetailEntity())
        private set
    var type = mutableStateOf("")
        private set
    var selectType = mutableStateOf(true)
        private set

    init {
        val indexes = savedStateHandle.get<String>("indexes") ?: ""
        val currentType = savedStateHandle.get<String>("type") ?: ""
        color = savedStateHandle.get<String>("color") ?: ""

        type.value = currentType
        selectDrinkDetails(indexes = indexes)
    }

    fun event(event: MenuDetailEvent) {
        when(event) {
            is MenuDetailEvent.TypeSelect -> {
                typeChane(event.select)
            }
        }
    }

    private fun selectDrinkDetails(indexes: String) {
        _list.clear()

        repository.selectDrinkDetails(indexes = indexes)
            .onEach {
                _list.addAll(it)
                when (type.value) {
                    Constants.IcedOnly, Constants.HotOnly -> {
                        item.value = _list[0]
                    }
                    else -> {
                        _list.find { listItem -> listItem.type == Constants.Hot }?.let { entity ->
                            item.value = entity
                        }
                    }
                }
            }
            .catch { _list.clear() }
            .launchIn(viewModelScope)
    }

    private fun typeChane(select: Boolean) {
        val type = if (select) Constants.Hot else Constants.Iced

        selectType.value = select
        _list.find { listItem -> listItem.type == type }?.let { entity ->
            item.value = entity
        }
    }

}