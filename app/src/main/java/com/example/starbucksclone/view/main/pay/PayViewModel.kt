package com.example.starbucksclone.view.main.pay

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val repository: CardRepository
): ViewModel() {

    private val _cardList = mutableStateListOf<CardEntity>()
    val cardList: List<CardEntity> = _cardList

    private var countDownTimer: CountDownTimer? = null
    private val _validTime = mutableStateOf("")
    val validTime: State<String> = _validTime

    init {
        selectCardList()
        startTimer()
    }

    private fun selectCardList() {
        _cardList.clear()
        repository.selectCardList()
            .onEach {
                _cardList.addAll(it)
                if (_cardList.none { card -> card.representative }) {
                    repository.updateRepresentative(
                        cardNumber = _cardList[0].cardNumber,
                        isRepresentative = true,
                        successListener = {},
                        failureListener = {}
                    )
                }
            }
            .catch { _cardList.clear() }
            .launchIn(viewModelScope)
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(600_000L, 1000) {
            override fun onTick(millisRemaining: Long) {
                _validTime.value = millisRemaining.formatTime()
            }

            override fun onFinish() {
                countDownTimer?.cancel()
            }
        }.start()
    }

}