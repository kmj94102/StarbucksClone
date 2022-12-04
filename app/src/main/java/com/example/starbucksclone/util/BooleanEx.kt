package com.example.starbucksclone.util

import java.util.regex.Pattern

fun String.isEmailFormat(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isKoreanFormat(): Boolean {
    val pattern = Pattern.compile("^[가-힣]*\$")
    return pattern.matcher(this).matches()
}