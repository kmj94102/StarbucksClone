package com.example.starbucksclone.view.main.order.menu_detail

sealed class MenuDetailEvent {
    data class TypeSelect(val select: Boolean): MenuDetailEvent()

}
