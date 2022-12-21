package com.example.starbucksclone.view.main.order.detail

sealed class MenuDetailEvent {
    data class HotIcedChange(val isHot: Boolean): MenuDetailEvent()
}