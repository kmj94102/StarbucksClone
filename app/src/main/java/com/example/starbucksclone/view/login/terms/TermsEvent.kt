package com.example.starbucksclone.view.login.terms

sealed class TermsEvent {
    object TermsOfServiceChange: TermsEvent()
    object PrivacyChange: TermsEvent()
    object PushChange: TermsEvent()
    object AllChange: TermsEvent()
}
