package com.example.starbucksclone.view.main.pay.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.CustomCheckBox
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun VoucherBottomSheetContents(
    completeListener: (String) -> Unit
) {
    val couponNumber = remember {
        mutableStateOf("")
    }
    val isChecked = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .background(White)
    ) {

        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(67.dp, 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(DarkGray)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "교환권 번호 입력하기",
            style = getTextStyle(16),
            modifier = Modifier
                .padding(top = 35.dp)
                .align(Alignment.CenterHorizontally)
        )

        CommonTextField(
            value = couponNumber.value,
            onValueChange = {
                if (it.length <= 18) {
                    couponNumber.value = it
                }
            },
            hint = "교환권 번호 (12자리 또는 18자리)",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, end = 23.dp, top = 45.dp)
        )

        Text(
            text = "스타벅스 카드 교환권 바코드 하단의 번호를 직접 입력해주세요.\n카카오톡 선물의 경우, 선물함에서 교환권 번호 우측 [복사하기] 기능을 이용하시면 더욱 편리합니다.",
            style = getTextStyle(size = 12, color = DarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
                .weight(1f)
        )

        Surface(
            elevation = 6.dp,
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp, start = 23.dp, end = 23.dp)
            ) {
                CustomCheckBox(
                    text = "스타벅스 카드 이용약관 동의 [필수]",
                    selected = isChecked.value,
                    onClick = { isChecked.value = isChecked.value.not() }
                )
                RoundedButton(
                    text = "등록하기",
                    isEnabled = isChecked.value && couponNumber.value.length in 12..18,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    completeListener(couponNumber.value)
                }
            }
        }
    }
}