package com.example.starbucksclone.view.main.order

import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.*
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CartRepository
import com.example.starbucksclone.repository.MyMenuRepository
import com.example.starbucksclone.repository.OrderMenuRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderMenuRepository,
    private val myMenuRepository: MyMenuRepository,
    private val cartRepository: CartRepository,
    private val pref: SharedPreferences
): ViewModel() {

    /** 메뉴 리스트 **/
    private val _menuList = mutableStateListOf<OrderMenuInfo>()
    val menuList: List<OrderMenuInfo> = _menuList

    /** 메뉴 그룹 선택 **/
    private val _selectState = mutableStateOf("음료")
    val selectState: State<String> = _selectState

    /** 나만의 메뉴 리스트 **/
    private val _myMenuList = mutableStateListOf<MyMenuInfo>()
    val myMenuList: List<MyMenuInfo> = _myMenuList

    /** 장바구니 카운트 **/
    private val _cartCount = mutableStateOf(0)
    val cartCount: State<Int> = _cartCount

    /** 상태 관리 **/
    private val _status = MutableStateFlow<OrderStatus>(OrderStatus.Init)
    val status: StateFlow<OrderStatus> = _status

    val id = pref.getLoginId() ?: ""

    init {
        selectOderMenu()
        selectMyMenuList()
        selectCartItemsCount()
    }

    fun event(event: OrderEvent) {
        when(event) {
            is OrderEvent.SelectChange -> {
                _selectState.value = event.select
                selectOderMenu()
            }
            is OrderEvent.Cart -> {
                insertCartItem(event.index)
            }
            is OrderEvent.Order -> {
                order(event.index)
            }
            is OrderEvent.StatusInit -> {
                _status.value = OrderStatus.Init
            }
        }
    }

    /** 메뉴 리스트 조회 **/
    private fun selectOderMenu() {
        repository.selectOrderMenu(_selectState.value)
            .onEach {
                _menuList.clear()
                _menuList.addAll(it.map { entity -> entity.mapper() })
            }
            .catch {
                _menuList.clear()
            }
            .launchIn(viewModelScope)
    }

    /** 나만의 메뉴 조회 **/
    private fun selectMyMenuList() {
        myMenuRepository.selectMyMenuList(id = id)
            .onEach {
                _myMenuList.clear()
                _myMenuList.addAll(it.map { entity -> entity.mapper() })
            }
            .catch { _myMenuList.clear() }
            .launchIn(viewModelScope)
    }

    /** 장바구니 리스트 조회 **/
    private fun selectCartItemsCount() {
        cartRepository.selectCartItemsCount(id = id)
            .onEach {
                _cartCount.value = it
            }
            .catch {
                _cartCount.value = 0
            }
            .launchIn(viewModelScope)
    }

    /** 장바구니 아이템 추가 **/
    private fun insertCartItem(index: Int) = viewModelScope.launch {
        cartRepository.insertCartItem(
            cartEntity = createCartEntity(index),
            successListener = {},
            failureListener = {
                _status.value = OrderStatus.Failure
            }
        )
    }

    /** 주문하기 **/
    private fun order(index: Int) {
        _status.value = OrderStatus.OrderInfo(
            Uri.encode(Gson().toJson(createCartEntity(index).paymentInfoMapper()))
        )
    }

    /** 장바구니 아이템 생성 **/
    private fun createCartEntity(index: Int) =
        CartEntity(
            id = id,
            menuIndex = _myMenuList[index].menuIndex,
            name = _myMenuList[index].name,
            nameEng = _myMenuList[index].nameEng,
            price = _myMenuList[index].price,
            image = _myMenuList[index].image,
            amount = 1,
            property = _myMenuList[index].property,
            date = System.currentTimeMillis()
        )

    sealed class OrderStatus {
        object Init: OrderStatus()
        data class OrderInfo(
            val info: String
        ): OrderStatus()
        object Failure: OrderStatus()
    }

}