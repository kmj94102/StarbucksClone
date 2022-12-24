package com.example.starbucksclone.view.main.order.detail

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.repository.MyMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val myMenuRepository: MyMenuRepository,
    private val pref: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var name = ""
    private var indexes = ""
    val id = pref.getLoginId()

    private val _isHotSelect = mutableStateOf(true)
    val isHotSelect: State<Boolean> = _isHotSelect
    private val _state = mutableStateOf(0)
    val state: State<Int> = _state
    private val _infoList = mutableStateListOf<MenuDetailInfo>()
    val info: State<MenuDetailInfo>
        get() = mutableStateOf(_infoList.getOrNull(_state.value) ?: MenuDetailInfo())

    private val _status = MutableStateFlow<MenuDetailStatus>(MenuDetailStatus.Init)
    val status: StateFlow<MenuDetailStatus> = _status

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
            is MenuDetailEvent.MyMenuRegister -> {
                insertMyMenu(event.myMenu)
            }
        }
    }

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

    sealed class MenuDetailStatus {
        object Init : MenuDetailStatus()
        object MyMenuSuccess : MenuDetailStatus()
        data class Failure(
            val msg: String
        ) : MenuDetailStatus()
    }

}