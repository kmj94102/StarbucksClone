package com.example.starbucksclone.view.main.home.rewards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.Constants
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.CustomTabRow
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.common.Progressbar
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun RewordScreen(routAction: RoutAction) {
    val state = rememberLazyListState()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        /** 타이틀 영역 **/
        stickyHeader {
            MainTitle(
                titleText = "Starbucks Rewards",
                isExpand = state.firstVisibleItemIndex < 1,
                onLeftIconClick = {
                    routAction.popupBackStack()
                }
            )
        }

        /** 텝 영역 **/
        item {
            CustomTabRow(
                pagerState = pagerState,
                tabItems = listOf("My Rewards", "How it works"),
                indicatorColor = Black,
                coroutineScope = scope
            )
        }

        /** 뷰페이저 영역 **/
        item {
            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (it) {
                    /** My Rewards 영역 **/
                    0 -> {
                        MyRewardsArea()
                    }
                    /** How It Works 영역 **/
                    1 -> {
                        HowItWorksArea()
                    }
                }
            }
        }
    }
}

/** My Rewards 영역 **/
@Composable
fun MyRewardsArea() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Black,
                        fontSize = 36.sp
                    )
                ) {
                    append("9")
                }
                withStyle(
                    style = SpanStyle(
                        color = DarkGray
                    )
                ) {
                    append(" / ")
                }
                append("30★")
            },
            style = getTextStyle(type = Constants.Text_24_Bold),
            color = MainColor,
            modifier = Modifier.padding(top = 30.dp, start = 22.dp)
        )

        Progressbar(
            current = 9,
            max = 30,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, end = 23.dp, top = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(LightGray)
        ) {
            Text(
                text = "Gold Level까지 21개의 별이 남았습니다.",
                style = getTextStyle(type = Constants.Text_12),
                modifier = Modifier.padding(horizontal = 23.dp, vertical = 20.dp)
            )
        }

        Text(
            text = "회원 등급 변경 및 쿠폰 발행은 최대 24시간이 걸릴 수 있습니다.",
            style = getTextStyle(type = Constants.Text_12),
            color = DarkGray,
            modifier = Modifier.padding(top = 10.dp, start = 23.dp, end = 23.dp)
        )

        Text(
            text = "멤버십 등급",
            style = getTextStyle(type = Constants.Text_16_Bold),
            modifier = Modifier.padding(top = 16.dp, start = 23.dp)
        )

        Text(
            text = "Green Level",
            style = getTextStyle(Constants.Text_26_Bold, MainColor),
            modifier = Modifier.padding(top = 16.dp, start = 23.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )

        Text(
            text = "등급별 혜택",
            style = getTextStyle(Constants.Text_16_Bold),
            modifier = Modifier.padding(start = 23.dp, top = 32.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 21.dp, start = 23.dp)
        ) {
            Text(text = "Welcome", style = getTextStyle(Constants.Text_16, DarkGray))
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(1.dp, 10.dp)
                    .background(DarkGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Green", style = getTextStyle(Constants.Text_16_Bold))
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(1.dp, 10.dp)
                    .background(DarkGray)
                    .padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Gold", style = getTextStyle(Constants.Text_16, DarkGray))
        }

        BenefitsByLevel(
            "생일 쿠폰 발행", modifier = Modifier
                .fillMaxWidth()
                .padding(top = 23.dp, start = 23.dp, end = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            "250g 원두 또는 12개입 VIA 구매 시, 카페 아메리카노 (Hot/Iced)쿠폰", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            "스타벅스 리저브 원두 구매시, 스타벅스 리저브 음료 또는 카페 아메리카노(Hot/Iced)쿠폰", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            "티바나 패키지 티 구매 시, 풀 리프티(Hot/Iced)쿠폰", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            "Beverage BOGO 쿠폰", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

/** 등급별 혜택 **/
@Composable
fun BenefitsByLevel(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(Gray)
    ) {
        Text(
            text = text,
            style = getTextStyle(Constants.Text_12_Bold),
            modifier = Modifier.padding(top = 11.dp, start = 14.dp, end = 14.dp)
        )
        Row(
            modifier = Modifier
                .padding(start = 14.dp, bottom = 11.dp, top = 11.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFCDB474))
            ) {
                Text(
                    text = "Gold",
                    fontSize = 10.sp,
                    color = White,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 3.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(MainColor)
            ) {
                Text(
                    text = "Green",
                    fontSize = 10.sp,
                    color = White,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 3.dp)
                )
            }
        }
    }
}

/** How It Works 영역 **/
@Composable
fun HowItWorksArea() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "반갑습니다!", style = getTextStyle(Constants.Text_20_Bold), lineHeight = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Starbucks Rewards 회원은 스타벅스 카드로 결제/주문할 때마다 다양한 별 적립 혜택을 받을 수 있습니다.")
        Spacer(modifier = Modifier.height(40.dp))

        HowItWorksItem(
            number = 1,
            content = "스타벅스 카드를 이용해 사이렌 오더 결제를 해보세요. 앱에서 스타벅스 카드 바코드를 띄워 매장에서 직접 결제할 수도 있습니다.",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 2,
            content = "스타벅스 카드로 결제 시 별이 적립됩니다.",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 3,
            content = "쌓이는 별에 따라 레벨이 오르고 골드 레벨부터 별 12개 적립 시마다 무료 음료 쿠폰을 제공합니다.",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 4,
            content = "스타벅스 리워드 회원만을 위한 특별한 이벤트에 참여하시면, 더 많은 별을 적립할 수 있습니다. 개인컵 사용, 브런치 유어 웨이 등을 통해서도 추가 별을 적립해 보세요!",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

/** How It Works 설명 **/
@Composable
fun HowItWorksItem(
    number: Int,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.img_reward),
            contentDescription = null,
            Modifier.size(95.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "$number", style = getTextStyle(Constants.Text_20_Bold))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = getTextStyle(Constants.Text_12),
                lineHeight = 20.sp
            )
        }
    }
}