package com.example.starbucksclone.view.main.order.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun MenuDetailScreen(routeAction: RouteAction) {
    val state = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    AsyncImage(
                        model = "https://image.istarbucks.co.kr/upload/store/skuimg/2022/10/[9200000002259]_20221007082850170.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFF9400))
                            .height(270.dp)
                    )
                }
                item {
                    MenuDetailBody()
                }
            }

            MenuDetailHeader(
                routeAction = routeAction,
                isExpand = state.firstVisibleItemIndex > 1
            )
        }
        FooterWithButton(text = "주문하기") {

        }
    }
}

@Composable
fun MenuDetailHeader(
    routeAction: RouteAction,
    isExpand: Boolean
) {
    when (isExpand) {
        true -> {
            Surface(
                elevation = 6.dp,
                color = White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .nonRippleClickable { routeAction.popupBackStack() }
                    )
                    Text(
                        text = "디카페인 카페 아메리카노",
                        style = if (isExpand) Typography.subtitle1 else Typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "share",
                        modifier = Modifier.padding(end = 17.dp)
                    )
                }
            }
        }
        false -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp, start = 12.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(Color(0x33000000))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back",
                        tint = White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .nonRippleClickable { routeAction.popupBackStack() }
                            .padding(end = 1.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp, end = 12.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(Color(0x33000000))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "share",
                        tint = White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuDetailBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    ) {
        Text(
            text = "디카페인 카페 아메리카노",
            style = getTextStyle(24, true, Black),
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "스타벅스의 깊고 강렬한 에스프레소의 풍미를 디카페인 카페 아메리카노로 즐겨보세요!",
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "디카페인 카페 아메리카노",
            style = getTextStyle(12, false, Black),
            modifier = Modifier.padding(top = 15.dp)
        )

        Text(
            text = 4400.priceFormat(),
            style = getTextStyle(20, true, Black),
            modifier = Modifier.padding(top = 20.dp)
        )

        val isHotSelected = remember {
            mutableStateOf(true)
        }

        HotOrIcedSelector(
            isHotSelected = isHotSelected.value,
            isOnly = true,
            modifier = Modifier.padding(top = 22.dp)
        ) {
            isHotSelected.value = it
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = "제품 영양 정보",
                style = getTextStyle(16, true, Black),
                modifier = Modifier.weight(1f)
            )
            Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = null)
        }

    }
}

@Composable
fun HotOrIcedSelector(
    isHotSelected: Boolean,
    isOnly: Boolean = false,
    modifier: Modifier = Modifier,
    selectedListener: (Boolean) -> Unit
) {
    if (isOnly) {
        RoundedButton(
            text = if (isHotSelected) "HOT ONLY" else "ICED ONLY",
            isOutline = true,
            buttonColor = DarkGray,
            textStyle = getTextStyle(14, true),
            textColor = if (isHotSelected) HotColor else IceColor,
            modifier = modifier
                .fillMaxWidth()
                .height(35.dp)
        ) {

        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .border(BorderStroke(1.dp, DarkGray), RoundedCornerShape(17.dp))
        ) {
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .background(if (isHotSelected) HotColor else White)
                    .nonRippleClickable { selectedListener(true) }
            ) {
                Text(
                    text = "HOT",
                    style = getTextStyle(14, true, if (isHotSelected) White else DarkGray),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .background(if (isHotSelected) White else IceColor)
                    .nonRippleClickable { selectedListener(false) }
            ) {
                Text(
                    text = "ICED",
                    style = getTextStyle(14, true, if (isHotSelected) DarkGray else White),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
