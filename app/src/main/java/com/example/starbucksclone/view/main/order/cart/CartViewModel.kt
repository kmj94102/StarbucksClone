package com.example.starbucksclone.view.main.order.cart

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
    private val pref: SharedPreferences
): ViewModel() {

    var id = ""
    private val _list = mutableStateListOf<CartEntity>()
    val list: List<CartEntity> = _list

    init {
        pref.getLoginId()?.let {
            id = it
            selectCartItems(it)
        }
    }

    fun event(event: CartEvent) {
        when(event) {
            is CartEvent.AmountChange -> {
                _list[event.index] = _list[event.index].copy(
                    amount = _list[event.index].amount + event.value
                )
            }
            is CartEvent.AllDeleteCartItems -> {
                allDeleteItems()
            }
            is CartEvent.DeleteCartItem -> {
                deleteCartItem(event.index)
            }
        }
    }

    private fun selectCartItems(
        id: String
    ) {
        repository.selectCartItems(id = id)
            .onEach {
                _list.clear()
                _list.addAll(it)
            }
            .catch {
                _list.clear()
            }
            .launchIn(viewModelScope)
    }

    private fun deleteCartItem(
        index: Int
    ) = viewModelScope.launch {
        repository.deleteCartItem(
            index = index,
            successListener = {
                selectCartItems(id)
            },
            failureListener = {

            }
        )
    }

    private fun allDeleteItems() = viewModelScope.launch {
        repository.allDeleteCartItems(
            id = id,
            successListener = {
                selectCartItems(id)
            },
            failureListener = {

            }
        )
    }

}