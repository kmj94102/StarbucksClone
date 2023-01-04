package com.example.starbucksclone.view.main.pay

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.di.getLoginId
import com.example.starbucksclone.repository.CardRepository
import com.example.starbucksclone.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val repository: CardRepository,
    private val pref: SharedPreferences
): ViewModel() {

    /** 카드 리스트 **/
    private val _cardList = mutableStateListOf<CardInfo>()
    val cardList: List<CardInfo> = _cardList

    /** 타이머 **/
    private var countDownTimer: CountDownTimer? = null
    private val _timer = mutableStateOf("")
    val timer: State<String> = _timer

    init {
        pref.getLoginId()?.let {
            selectCardList(it)
        }
    }

    /** 카드 리스트 조회 **/
    private fun selectCardList(id: String) {
        repository.selectCardList(id)
            .onEach {
                _cardList.clear()
                _cardList.addAll(it.map { entity -> entity.mapper() })
                startTimer()
            }
            .catch { _cardList.clear() }
            .launchIn(viewModelScope)
    }

    /** 인증번호 타이머 실행 **/
    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(600_000L, 1_000) {
            override fun onTick(millisRemaining: Long) {
                _timer.value = millisRemaining.formatTime()
            }

            override fun onFinish() {
                countDownTimer?.cancel()
            }
        }.start()
    }

}