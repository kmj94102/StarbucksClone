package com.example.starbucksclone.view.main.pay.registration

sealed class CardRegistrationEvent {

    data class InputInfo(
        val type: String,
        val info: String
    ): CardRegistrationEvent()

}