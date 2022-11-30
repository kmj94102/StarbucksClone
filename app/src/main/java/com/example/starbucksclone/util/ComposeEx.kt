package com.example.starbucksclone.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.ui.theme.Black

@Composable
fun Modifier.nonRippleClickable(
    onClick: () -> Unit
) = clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() }
) {
    onClick()
}

@Composable
fun getColorFromHexCode(code: String) =
    Color(android.graphics.Color.parseColor("#$code"))

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

fun getTextStyle(
    type: String,
    color: Color = Black
): TextStyle {
    val size = type.replace("text", "")
        .replace("bold", "")
        .trim()
        .toInt().sp

    return TextStyle(
        fontSize = size,
        fontWeight = if (type.contains("bold")) FontWeight.Bold else FontWeight.Normal,
        color = color
    )
}