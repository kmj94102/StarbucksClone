package com.example.starbucksclone.view.main.order.detail

import com.example.starbucksclone.database.entity.MyMenuEntity

sealed class MenuDetailEvent {
    data class HotIcedChange(val isHot: Boolean): MenuDetailEvent()
    data class MyMenuRegister(val myMenu: MyMenuEntity): MenuDetailEvent()
}