package com.example.starbucksclone.view.main.pay.registration

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.util.specialCharacterRestrictions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardRegistrationViewModel @Inject constructor(
    private val repository: CardRepository
): ViewModel() {

    var cardRegistrationInfo = mutableStateOf(CardRegistrationInfo())
        private set
    private val _isCardNumberError = mutableStateOf(false)
    val isCardNumberError: State<Boolean> = _isCardNumberError

    private val _isPinNumberError = mutableStateOf(false)
    val isPinNumberError: State<Boolean> = _isPinNumberError

    private val _isAgreeTerms = mutableStateOf(false)
    val isAgreeTerms: State<Boolean> = _isAgreeTerms

    private val _status = MutableStateFlow<Status>(Status.Init)
    val status: StateFlow<Status> = _status

    private val _isEnable = mutableStateOf(false)
    val isEnable: State<Boolean> = _isEnable

    fun event(event: CardRegistrationEvent) {
        when(event) {
            is CardRegistrationEvent.InputInfo -> {
                inputInfo(event)
                checkEnabled()
            }
            is CardRegistrationEvent.AgreeTermsCheck -> {
                _isAgreeTerms.value = _isAgreeTerms.value.not()
                checkEnabled()
            }
            is CardRegistrationEvent.CardRegistration -> {
                caretCard()
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

    private fun caretCard() = viewModelScope.launch {
        _status.value = Status.Loading
        repository.createCard(
            info = cardRegistrationInfo.value,
            successListener = {
                _status.value = Status.Success
            },
            failureListener = {
                _status.value = Status.Failure
            }
        )
    }

    private fun checkEnabled() {
        _isEnable.value = _isAgreeTerms.value &&
                cardRegistrationInfo.value.cardNumber.length == 16 &&
                cardRegistrationInfo.value.pinNumber.length == 8
    }

    sealed class Status {
        object Init: Status()
        object Loading: Status()
        object Success: Status()
        object Failure: Status()
    }

    companion object {
        const val CardName = "card_name"
        const val CardNumber = "card_number"
        const val PinNumber = "pin_number"
    }

}