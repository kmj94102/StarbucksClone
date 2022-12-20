package com.example.starbucksclone.view.main.order.menu_list

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
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _group = ""
    private var _name = mutableStateOf("")
    val name: State<String> = _name

    private val _list = mutableStateListOf<MenuEntity>()
    val list: List<MenuEntity> = _list

    init {
        savedStateHandle.get<String>("group")?.let {
            _group = it
        }
        savedStateHandle.get<String>("name")?.let {
            _name.value = it
        }
        when(_name.value) {
            "New" -> {
                selectNewMenuList()
            }
            "추천" -> {
                selectRecommendMenuList()
            }
            else -> {
                selectMenuList()
            }
        }
    }

    /** 메뉴 조회 **/
    private fun selectMenuList() {
        repository.selectMenuList(
            group = _group,
            name = _name.value
        ).onEach {
            _list.clear()
            _list.addAll(it)
        }.catch {
            _list.clear()
        }.launchIn(viewModelScope)
    }

    /** New 메뉴 조회 **/
    private fun selectNewMenuList() {
        repository.selectNewMenuList(
            group = _group,
        ).onEach {
            _list.clear()
            _list.addAll(it)
        }.catch {
            _list.clear()
        }.launchIn(viewModelScope)
    }

    /** 추천 메뉴 조회 **/
    private fun selectRecommendMenuList() {
        repository.selectRecommendMenuList(
            group = _group,
        ).onEach {
            _list.clear()
            _list.addAll(it)
        }.catch {
            _list.clear()
        }.launchIn(viewModelScope)
    }
}