package com.example.starbucksclone.view.main.order.detail

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CartRepository
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.repository.MyMenuRepository
import com.example.starbucksclone.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val myMenuRepository: MyMenuRepository,
    private val cartRepository: CartRepository,
    private val pref: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var name = ""
    private var indexes = ""
    val id = pref.getLoginId()

    /** Hot 선택 여부 **/
    private val _isHotSelect = mutableStateOf(true)
    val isHotSelect: State<Boolean> = _isHotSelect

    private val _state = mutableStateOf(0)
    private val _infoList = mutableStateListOf<MenuDetailInfo>()
    val info: State<MenuDetailInfo>
        get() = mutableStateOf(_infoList.getOrNull(_state.value) ?: MenuDetailInfo())

    private val _status = MutableStateFlow<MenuDetailStatus>(MenuDetailStatus.Init)
    val status: StateFlow<MenuDetailStatus> = _status

    init {
        savedStateHandle.get<String>(Constants.Indexes)?.let {
            indexes = it
        }
        savedStateHandle.get<String>(Constants.Name)?.let {
            name = it
        }
        selectMenuDetail()
    }

    /** 메뉴 상세 조회 **/
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
            is MenuDetailEvent.MyMenuRegister -> {
                insertMyMenu(event.myMenu)
            }
            is MenuDetailEvent.AddCartItem -> {
                insertCartItem(event.cartEntity)
            }
        }
    }

    /** 나만의 메뉴 등록 **/
    private fun insertMyMenu(
        myMenuEntity: MyMenuEntity
    ) = viewModelScope.launch {
        myMenuRepository.insertMyMenu(
            myMenuEntity = myMenuEntity,
            successListener = {
                _status.value = MenuDetailStatus.MyMenuSuccess
            },
            failureListener = {
                _status.value = MenuDetailStatus.Failure("나만의 메뉴 등록에 실패하였습니다.")
            }
        )
        delay(100)
        _status.value = MenuDetailStatus.Init
    }

    /** 장바구니 등록 **/
    private fun insertCartItem(
        cartEntity: CartEntity
    ) = viewModelScope.launch {
        cartRepository.insertCartItem(
            cartEntity = cartEntity,
            successListener = {
                _status.value = MenuDetailStatus.CartAdditionSuccess
            },
            failureListener = {
                _status.value = MenuDetailStatus.Failure("장바구니 등록을 실패하였습니다.")
            }
        )
    }

    sealed class MenuDetailStatus {
        object Init : MenuDetailStatus()
        object MyMenuSuccess : MenuDetailStatus()
        data class Failure(
            val msg: String
        ) : MenuDetailStatus()
        object CartAdditionSuccess: MenuDetailStatus()
    }

}