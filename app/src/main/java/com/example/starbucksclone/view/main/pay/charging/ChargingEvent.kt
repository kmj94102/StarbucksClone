package com.example.starbucksclone.view.main.pay.charging

sealed class ChargingEvent {
    data class ChargingAmountChange(
        val amount: Long
    ): ChargingEvent()

    object CardCharging: ChargingEvent()
}