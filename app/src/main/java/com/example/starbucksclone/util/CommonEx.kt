package com.example.starbucksclone.util

import android.content.Context
import android.content.res.AssetManager
import android.widget.Toast
import androidx.annotation.StringRes
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.text.DecimalFormat
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

fun AssetManager.readCSV(fileName: String) =
    CSVReader(InputStreamReader(open(fileName))).readAll()
        .map { it.toList() }
        .filterIndexed { index, _ -> index != 0 }

fun Int.priceFormat() = DecimalFormat("###,###").format(this).plus("원")
