package com.example.starbucksclone.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

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

fun getEmoji(unicode: Int): String = String(Character.toChars(unicode))

fun getStarbucksCardImage(): String {
    val list = listOf(
        "https://image.istarbucks.co.kr/cardImg/20220907/009446_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220825/009387_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220712/009227_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220712/009226_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220712/009228_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220711/009207_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220620/009153_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220425/009067_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220520/008988_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220421/008987_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220413/008986_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220315/008947_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220218/008927_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220218/008926_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220214/008906_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220204/008886_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220203/008866_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211231/008726_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211227/008707_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220104/008750_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20220104/008749_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211227/008706_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211227/008686_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211213/008667_WEB.png",
        "https://image.istarbucks.co.kr/cardImg/20211213/008666_WEB.png"
    )

    return list.random()
}