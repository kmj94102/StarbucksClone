package com.example.starbucksclone.view.main.order.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun CartAdditionCompleteBottomSheet(
    goToCartListener: () -> Unit,
    finishListener: () -> Unit,
    closeListener: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "장바구니에 추가되었습니다.", style = getTextStyle(18, true))
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.nonRippleClickable { closeListener() }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            RoundedButton(
                text = "장바구니 가기",
                isOutline = true,
                textColor = MainColor,
                modifier = Modifier.weight(1f)
            ) {
                goToCartListener()
            }
            RoundedButton(
                text = "다른 메뉴 더보기",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                finishListener()
            }
        }
    }
}