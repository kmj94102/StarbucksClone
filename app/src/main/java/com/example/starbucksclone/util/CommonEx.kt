package com.example.starbucksclone.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

private const val Time_Format = "%02d:%02d"
fun Long.formatTime(): String = String.format(
    Time_Format,
    TimeUnit.MILLISECONDS.toMinutes(this),
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

fun today(): String {
    val format = DateTimeFormatter.ofPattern("yyyy년 MM월 dd")
    val date = LocalDateTime.now()
    return date.format(format)
}

fun specialCharacterRestrictions(source: String): Boolean {
    return try {
        val pattern = Pattern.compile("^[0-9]*$")
        pattern.matcher(source).matches()
    } catch (e: Exception) {
        e.printStackTrace()
        true
    }
}

fun koreanCheck(value: String): Boolean {
    val pattern = Pattern.compile("^[가-힣]*\$")
    return pattern.matcher(value).matches()
}