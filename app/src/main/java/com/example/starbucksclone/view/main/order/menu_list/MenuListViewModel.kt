package com.example.starbucksclone.view.main.order.menu_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.database.entity.MenuInfo
import com.example.starbucksclone.repository.MenuRepository
import com.example.starbucksclone.util.Constants
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

    /** 메뉴 그룹 **/
    private var _group = ""
    /** 선택 이름 **/
    private var _name = mutableStateOf("")
    val name: State<String> = _name

    private val _list = mutableStateListOf<MenuInfo>()
    val list: List<MenuInfo> = _list

    init {
        savedStateHandle.get<String>(Constants.Group)?.let {
            _group = it
        }
        savedStateHandle.get<String>(Constants.Name)?.let {
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
            _list.addAll(it.map { entity -> entity.mapper() })
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
            _list.addAll(it.map { entity -> entity.mapper() })
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
            _list.addAll(it.map { entity -> entity.mapper() })
        }.catch {
            _list.clear()
        }.launchIn(viewModelScope)
    }
}