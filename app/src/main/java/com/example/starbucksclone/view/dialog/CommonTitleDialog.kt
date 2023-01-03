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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.view.common.RoundedButton

/**
 * @param title 타이틀 문구
 * @param isShow 다이얼로그 활성화 여부
 * @param okText 확인 버튼 텍스트
 * @param cancelText 취소 버튼 텍스트
 * @param isCancelVisible 취소 버튼 표시 여부
 * @param okClickListener 확인 버튼 클릭 리스너
 * @param okButtonEnable 확인 버튼 활성화 여부
 * @param cancelClickListener 취소 버튼 클릭 리스너
 * @param contentPadding 텍스트와 내용 사이의 패딩
 * **/
@Composable
fun CommonTitleDialog(
    title: String,
    isShow: Boolean,
    okText: String = stringResource(id = R.string.yes),
    cancelText: String = stringResource(id = R.string.no),
    isCancelVisible: Boolean = true,
    okClickListener: (() -> Unit)? = null,
    okButtonEnable: Boolean = true,
    cancelClickListener: (() -> Unit)? = null,
    contentPadding: Dp = 33.dp,
    contents: @Composable () -> Unit = {}
) {
    if (isShow) {
        Dialog(
            onDismissRequest = { }
        ) {
            Surface(
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                ) {
                    Spacer(modifier = Modifier.height(43.dp))
                    Text(
                        text = title,
                        style = getTextStyle(16, true),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    )

                    Spacer(modifier = Modifier.height(contentPadding))
                    contents()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        if (isCancelVisible) {
                            RoundedButton(
                                text = cancelText,
                                textColor = MainColor,
                                isOutline = true
                            ) {
                                cancelClickListener?.invoke()
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                        RoundedButton(
                            text = okText,
                            textColor = White,
                            isEnabled = okButtonEnable
                        ) {
                            okClickListener?.invoke()
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}