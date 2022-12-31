package com.example.starbucksclone.view.main.order

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CartRepository
import com.example.starbucksclone.repository.MyMenuRepository
import com.example.starbucksclone.repository.OrderMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderMenuRepository,
    private val myMenuRepository: MyMenuRepository,
    private val cartRepository: CartRepository,
    private val pref: SharedPreferences
): ViewModel() {

    private val _list = mutableStateListOf<OrderMenuEntity>()
    val list: List<OrderMenuEntity> = _list

    private val _selectState = mutableStateOf("음료")
    val selectState: State<String> = _selectState

    private val _myMenuList = mutableStateListOf<MyMenuEntity>()
    val myMenuList: List<MyMenuEntity> = _myMenuList

    private val _cartCount = mutableStateOf(0)
    val cartCount: State<Int> = _cartCount

    val id = pref.getLoginId() ?: ""

    init {
        selectOderMenu()
        selectMyMenuList()
        selectCartItems()
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

    private fun selectMyMenuList() {
        myMenuRepository.selectMyMenuList(id = id)
            .onEach {
                _myMenuList.clear()
                _myMenuList.addAll(it)
            }
            .catch { _myMenuList.clear() }
            .launchIn(viewModelScope)
    }

    private fun selectCartItems() {
        cartRepository.selectCartItems(id = id)
            .onEach {
                _cartCount.value = it.size
            }
            .catch {
                _cartCount.value = 0
            }
            .launchIn(viewModelScope)
    }

}