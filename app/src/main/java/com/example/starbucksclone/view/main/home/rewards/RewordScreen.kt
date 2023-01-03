package com.example.starbucksclone.view.main.home.rewards

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.view.common.CustomTabRow
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.common.Progressbar
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun RewordScreen(routeAction: RouteAction) {
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
                titleText = stringResource(id = R.string.starbucks_rewards),
                isExpand = state.firstVisibleItemIndex < 1,
                onLeftIconClick = {
                    routeAction.popupBackStack()
                }
            )
        }
        /** 텝 영역 **/
        item {
            CustomTabRow(
                pagerState = pagerState,
                tabItems = listOf(
                    stringResource(id = R.string.my_rewards),
                    stringResource(id = R.string.how_it_works)
                ),
                indicatorColor = Black,
                coroutineScope = scope
            )
        }

        /** 뷰페이저 영역 **/
        item {
            HorizontalPager(
                count = 2,
                state = pagerState,
                verticalAlignment = Alignment.Top,
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
            style = getTextStyle(24, true),
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
                text = stringResource(id = R.string.next_level_guide),
                style = getTextStyle(12),
                modifier = Modifier.padding(horizontal = 23.dp, vertical = 20.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.grade_change_guide),
            style = getTextStyle(12),
            color = DarkGray,
            modifier = Modifier.padding(top = 10.dp, start = 23.dp, end = 23.dp)
        )

        Text(
            text = stringResource(id = R.string.membership_grade),
            style = getTextStyle(16, true),
            modifier = Modifier.padding(top = 16.dp, start = 23.dp)
        )

        Text(
            text = stringResource(id = R.string.green_level),
            style = getTextStyle(16, true, MainColor),
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
            text = stringResource(id = R.string.benefits_by_grade),
            style = getTextStyle(16, true),
            modifier = Modifier.padding(start = 23.dp, top = 32.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 21.dp, start = 23.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                style = getTextStyle(size = 16, color = DarkGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(1.dp, 10.dp)
                    .background(DarkGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.green),
                style = getTextStyle(16, true)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(1.dp, 10.dp)
                    .background(DarkGray)
                    .padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.gold),
                style = getTextStyle(size = 16, color = DarkGray)
            )
        }

        BenefitsByLevel(
            text = stringResource(id = R.string.benefits_by_grade_guide1),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 23.dp, start = 23.dp, end = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            text = stringResource(id = R.string.benefits_by_grade_guide2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            text = stringResource(id = R.string.benefits_by_grade_guide3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            text = stringResource(id = R.string.benefits_by_grade_guide4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        BenefitsByLevel(
            text = stringResource(id = R.string.benefits_by_grade_guide5),
            modifier = Modifier
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
            style = getTextStyle(12, true),
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
                    text = stringResource(id = R.string.gold),
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
                    text = stringResource(id = R.string.green),
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
        Text(
            text = stringResource(id = R.string.welcome_k),
            style = getTextStyle(20, true),
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = stringResource(id = R.string.how_it_work_guide0))
        Spacer(modifier = Modifier.height(40.dp))

        HowItWorksItem(
            number = 1,
            imageRes = R.drawable.img_works1,
            content = stringResource(id = R.string.how_it_work_guide1),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 2,
            imageRes = R.drawable.img_works2,
            content = stringResource(id = R.string.how_it_work_guide2),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 3,
            imageRes = R.drawable.img_works3,
            content = stringResource(id = R.string.how_it_work_guide3),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))

        HowItWorksItem(
            number = 4,
            imageRes = R.drawable.img_works4,
            content = stringResource(id = R.string.how_it_work_guide4),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

/** How It Works 설명 **/
@Composable
fun HowItWorksItem(
    number: Int,
    @DrawableRes imageRes: Int,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            Modifier.size(95.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "$number", style = getTextStyle(20, true))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = getTextStyle(12),
                lineHeight = 20.sp
            )
        }
    }
}