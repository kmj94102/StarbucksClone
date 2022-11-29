package com.example.starbucksclone.view.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun HomeScreen() {
    val state = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            guestHomeInfo(state)

            item {
                HomeNewMenu()
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.img_event_banner1),
                    contentDescription = "banner",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(id = R.drawable.img_event_banner2),
                    contentDescription = "banner",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        DeliversFloatingButton(
            isExpand = state.firstVisibleItemIndex < 1,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 23.dp, bottom = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.guestHomeInfo(
    state: LazyListState
) {
    item {
        Text(
            text = "안녕하세요.\n스타벅스입니다.",
            style = Typography.subtitle1,
            modifier = Modifier.padding(top = 52.dp, start = 23.dp)
        )
    }

    item {
        Surface(
            elevation = 6.dp,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, start = 10.dp, end = 10.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "스타벅스 리워드\n회원 신규가입 첫 구매 시,\n무료음료 혜택을 드려요!",
                    style = Typography.body1,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 36.dp, start = 30.dp)
                )

                RoundedButton(
                    text = "회원가입",
                    modifier = Modifier.padding(top = 120.dp, start = 30.dp, bottom = 35.dp)
                ) {

                }

                RoundedButton(
                    text = "로그인",
                    textColor = MainColor,
                    isOutline = true,
                    modifier = Modifier.padding(top = 120.dp, start = 130.dp)
                ) {

                }

                Image(
                    painter = painterResource(id = R.drawable.img_reward),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 41.dp, end = 13.dp)
                        .align(Alignment.TopEnd)
                        .size(109.dp, 112.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

    stickyHeader {
        Surface(
            color = White,
            elevation = if (state.firstVisibleItemIndex > 1) 6.dp else 0.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 23.dp, vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_messgae),
                    contentDescription = null
                )

                Text(
                    text = "What's New", modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f)
                )

                Icon(painter = painterResource(id = R.drawable.ic_bell), contentDescription = null)
            }
        }
    }
}

fun LazyListScope.userHomeInfo() {

}

@Composable
fun HomeNewMenu() {
    val tempMenuList = listOf(
        "디카페인 카페 아메리카노", "아이스 디카페인 카페 아메리카노", "카페 아메리카노",
        "디카페인 카페 아메리카노", "아이스 디카페인 카페 아메리카노", "카페 아메리카노",
        "디카페인 카페 아메리카노", "아이스 디카페인 카페 아메리카노", "카페 아메리카노"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Text(
            text = "새로 나온 메뉴",
            style = Typography.subtitle2,
            modifier = Modifier.padding(start = 23.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                tempMenuList.forEach {
                    HomeNewMenuItem(name = it)
                }
            }
        }
    }
}

@Composable
fun HomeNewMenuItem(name: String) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        CircleImage(
            imageURL = "https://image.istarbucks.co.kr/upload/store/skuimg/2022/10/[9200000002259]_20221007082850170.jpg",
            backgroundColor = MainColor,
        )
        Text(
            text = name,
            style = Typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 11.dp)
                .width(110.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DeliversFloatingButton(
    isExpand: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { },
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp
        ),
        contentPadding = PaddingValues(
            start = if (isExpand) 25.dp else 0.dp,
            end = if (isExpand) 20.dp else 0.dp,
            top = 16.dp,
            bottom = 16.dp
        ),
        shape = RoundedCornerShape(54.dp),
        modifier = modifier
            .height(54.dp)
            .widthIn(min = 54.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_delivers),
            contentDescription = "delivers",
            modifier = Modifier
                .padding(end = if (isExpand) 10.dp else 0.dp)
                .align(Alignment.CenterVertically)
        )
        if (isExpand) {
            Text(
                text = "Delivers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
            )
        }
    }
}