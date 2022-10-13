package com.example.starbucksclone.view.login.terms

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor() : ViewModel() {
    private val _termsOfServiceState = mutableStateOf(false)
    val termsOfServiceState: State<Boolean> = _termsOfServiceState

    private val _privacyState = mutableStateOf(false)
    val privacyState: State<Boolean> = _privacyState

    private val _pushState = mutableStateOf(false)
    val pushState: State<Boolean> = _pushState

    fun event(event: TermsEvent) {
        when(event) {
            is TermsEvent.AllChange -> {
                val isChecked = (_termsOfServiceState.value && _privacyState.value && _pushState.value).not()
                _termsOfServiceState.value = isChecked
                _privacyState.value = isChecked
                _pushState.value = isChecked
            }
            is TermsEvent.TermsOfServiceChange -> {
                _termsOfServiceState.value = _termsOfServiceState.value.not()
            }
            is TermsEvent.PrivacyChange -> {
                _privacyState.value = _privacyState.value.not()
            }
            is TermsEvent.PushChange -> {
                _pushState.value = _pushState.value.not()
            }
        }
    }

}