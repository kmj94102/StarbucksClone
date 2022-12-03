package com.example.starbucksclone.util

import java.util.regex.Pattern

fun String.isEmailFormat(): Boolean {
    return Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$").matcher(this).matches()
}

fun String.isKoreanFormat(): Boolean {
    val pattern = Pattern.compile("^[가-힣]*\$")
    return pattern.matcher(this).matches()
}