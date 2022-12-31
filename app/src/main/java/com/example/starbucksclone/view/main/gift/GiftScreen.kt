package com.example.starbucksclone.view.main.gift

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun GiftScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        GiftHeader()
        LazyColumn {
            item {
                GiftBanner()
            }
            item {
                GiftBody()
            }
        }
    }
}

@Composable
fun GiftHeader() {
    MainTitle(
        titleText = "Gift",
        isExpand = false,
        leftIconRes = null,
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = "search",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 17.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 55.dp)
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GiftBanner() {
    val list = listOf(
        R.drawable.img_gift_banner1,
        R.drawable.img_gift_banner2,
        R.drawable.img_gift_banner3
    )
    val state = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainColor)
    ) {
        HorizontalPager(
            count = list.size,
            state = state
        ) {
            GiftBannerItem(list[it])
        }
        HorizontalPagerIndicator(
            pagerState = state,
            activeColor = Black,
            inactiveColor = White,
            indicatorWidth = 5.dp,
            indicatorHeight = 5.dp,
            spacing = 8.dp,
            indicatorShape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp)
        )
    }
}

@Composable
fun GiftBannerItem(@DrawableRes res: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = res),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
                .height(152.dp)
        )
    }
}

@Composable
fun GiftBody() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "테마 선물",
            style = getTextStyle(18, true),
            modifier = Modifier.padding(top = 20.dp, start = 15.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        GiftBodyItem(
            title = "함께해서 더 빛나는 우리,",
            content = "BRIGHTEN YOUR HOLIDAYS",
            color = Color(0xFF6D0A01)
        )
        Spacer(modifier = Modifier.height(10.dp))
        GiftBodyItem(
            title = "기온 뚝",
            content = "쌀쌀한 날씨, 감기 조심하세요~!",
            color = Color(0xFF942F00)
        )
        Spacer(modifier = Modifier.height(10.dp))
        GiftBodyItem(
            title = "소중한 사람이 떠오르는 연말,",
            content = "따듯한 마음을 전해보세요",
            color = Color(0xFF00490C)
        )
    }
}

@Composable
fun GiftBodyItem(
    title: String,
    content: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
    ) {
        Text(
            text = title,
            style = getTextStyle(18, false, White),
            modifier = Modifier.padding(top = 26.dp, start = 20.dp)
        )
        Text(
            text = content,
            style = getTextStyle(12, false, Gray),
            modifier = Modifier.padding(top = 5.dp, start = 20.dp, bottom = 27.dp)
        )
    }
}