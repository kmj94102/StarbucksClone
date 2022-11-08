package com.example.starbucksclone.view.main.pay.registration

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.util.specialCharacterRestrictions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardRegistrationViewModel @Inject constructor(

): ViewModel() {

    var cardRegistrationInfo = mutableStateOf(CardRegistrationInfo())
        private set
    private val _isCardNumberError = mutableStateOf(false)
    val isCardNumberError: State<Boolean> = _isCardNumberError

    private val _isPinNumberError = mutableStateOf(false)
    val isPinNumberError: State<Boolean> = _isPinNumberError

    fun event(event: CardRegistrationEvent) {
        when(event) {
            is CardRegistrationEvent.InputInfo -> {
                inputInfo(event)
            }
        }
    }

    private fun inputInfo(info: CardRegistrationEvent.InputInfo) {
        when(info.type) {
            CardName -> {
                cardRegistrationInfo.value = cardRegistrationInfo.value.copy(
                    cardName = info.info
                )
            }
            CardNumber -> {
                if (info.info.length > 16) return
                if (specialCharacterRestrictions(info.info).not()) return

                cardRegistrationInfo.value = cardRegistrationInfo.value.copy(
                    cardNumber = info.info
                )
                _isCardNumberError.value = info.info.length != 16
            }
            PinNumber -> {
                if (info.info.length > 8) return
                if (specialCharacterRestrictions(info.info).not()) return

                cardRegistrationInfo.value = cardRegistrationInfo.value.copy(
                    pinNumber = info.info
                )
                _isPinNumberError.value = info.info.length != 8
            }
        }
    }

    companion object {
        const val CardName = "card_name"
        const val CardNumber = "card_number"
        const val PinNumber = "pin_number"
    }

}