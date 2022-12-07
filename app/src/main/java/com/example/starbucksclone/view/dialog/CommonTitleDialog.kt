package com.example.starbucksclone.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun CommonTitleDialog(
    title: String,
    isShow: Boolean,
    okText: String = "예",
    cancelText: String = "아니오",
    okClickListener: (() -> Unit)? = null,
    cancelClickListener: (() -> Unit)? = null,
    contents: @Composable () -> Unit = {}
) {
    if (isShow) {
        Dialog(
            onDismissRequest = { }
        ) {
            Surface(
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(White)
                ) {
                    Spacer(modifier = Modifier.height(43.dp))
                    Text(
                        text = title,
                        style = Typography.body2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    )

                    Spacer(modifier = Modifier.height(33.dp))
                    contents()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        RoundedButton(text = cancelText, textColor = MainColor, isOutline = true) {
                            cancelClickListener?.invoke()
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        RoundedButton(text = okText, textColor = White) {
                            okClickListener?.invoke()
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}