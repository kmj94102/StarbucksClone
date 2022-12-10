package com.example.starbucksclone.view.main.other

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtherScreen() {
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(bottom = 150.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            /** 해더 영역 **/
            OtherHeader()
        }
        item {
            /** 바디 영역 **/
            OtherBody()
        }
    }
}

/** 해더 영역 **/
@Composable
fun OtherHeader() {
    MainTitle(
        titleText = "Other",
        leftIconRes = null,
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "list",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 17.dp)
                    .nonRippleClickable {
                    }
            )
        }
    )
}

/** 바디 영역 **/
@Composable
fun OtherBody() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "서비스",
            style = getTextStyle(16, true),
            modifier = Modifier.padding(top = 28.dp, start = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OtherBodyItem(
            iconRes = R.drawable.ic_rewad_star,
            text = "리워드"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_coupon,
            text = "쿠폰"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_gift_card,
            text = "e-기프트 카드"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_messgae,
            text = "What's New"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_bell,
            iconSize = 28.dp,
            text = "알림"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_history,
            iconSize = 24.dp,
            text = "히스토리"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_receipt,
            text = "전자영수증"
        ) {}
        OtherBodyItem(
            iconRes = R.drawable.ic_review,
            text = "마이 스타벅스 리뷰"
        ) {}
    }
}

@Composable
fun OtherBodyItem(
    @DrawableRes iconRes: Int,
    iconSize: Dp = 24.dp,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(start = 27.dp, end = 15.dp)
            .nonRippleClickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = text,
            style = getTextStyle(14),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 13.dp)
        )
    }
}