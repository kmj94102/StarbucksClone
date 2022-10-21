package com.example.starbucksclone.view.main.order

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.repository.OrderMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderMenuRepository
): ViewModel() {

    private val _allMenuSelected = mutableStateOf(Drink)
    val allMenuSelected: State<String> = _allMenuSelected

    private val _allMenuItemList = mutableStateListOf<OrderMenuEntity>()
    val allMenuItemList: List<OrderMenuEntity> = _allMenuItemList

    init {
        selectOrderMenu()
    }

    fun event(event: OrderEvent) {
        when(event) {
            is OrderEvent.SelectGroup -> {
                _allMenuSelected.value = event.group
                selectOrderMenu()
            }
        }
    }

    private fun selectOrderMenu() {
        _allMenuItemList.clear()
        repository
            .selectOrderMenu()
            .onEach {
                _allMenuItemList.addAll(
                    it.filter { menu ->
                        menu.group == _allMenuSelected.value
                    }
                )
            }
            .catch { cause ->
                cause.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    companion object {
        const val Drink = "drink"
        const val Food = "food"
        const val Product = "product"
    }

}