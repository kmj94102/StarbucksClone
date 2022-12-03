package com.example.starbucksclone.util

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
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

// 출처 :  https://stackoverflow.com/questions/68847559/how-can-i-detect-keyboard-opening-and-closing-in-jetpack-compose
enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
fun getColorFromHexCode(code: String) =
    Color(android.graphics.Color.parseColor("#$code"))

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

fun getTextStyle(
    size: Int = 14,
    isBold: Boolean = false,
    color: Color = Black
) = TextStyle(
    fontSize = size.sp,
    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
    color = color
)
