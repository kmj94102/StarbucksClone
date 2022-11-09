package com.example.starbucksclone.view.main.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getEmoji
import com.example.starbucksclone.view.common.RoundedButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)
    ) {
        item {
            viewModel.nickname?.let {
                HomeHeaderLoginUser(it)
            } ?: HomeHeader()
        }

        stickyHeader {
            Row(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 23.dp, vertical = 15.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_messgae),
                    contentDescription = "message"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "What's New", style = Typography.body2)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = "bell",
                )
            }
        }

        item {
            Text(
                text = "새로 나온 메뉴",
                style = Typography.subtitle2,
                modifier = Modifier.padding(top = 40.dp, bottom = 30.dp, start = 23.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                contentPadding = PaddingValues(horizontal = 23.dp)
            ) {
                val range = (0..10)
                range.forEach { _ ->
                    item { HomeMenuItem() }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillParentMaxSize()
                    .background(DarkGray)
            )
            Box(
                modifier = Modifier
                    .fillParentMaxSize()
                    .background(MainColor)
            )
        }
    }
}

@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(52.dp))
        Text(
            text = "안녕하세요.\n스타벅스입니다.",
            style = Typography.subtitle1,
            modifier = Modifier.padding(start = 23.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedCard(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = White
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 10.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(
                    top = 35.dp,
                    bottom = 35.dp,
                    start = 26.dp,
                    end = 12.dp
                )
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "스타벅스 리워드\n회원 신규 가입 첫 구매 시,\n무료음료 혜택드려요!",
                        style = Typography.body1,
                        lineHeight = 28.sp,
                        color = DarkGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                        RoundedButton(
                            text = "회원가입",
                        ) {

                        }
                        RoundedButton(
                            text = "로그인",
                            isOutline = true,
                            textColor = MainColor,
                            modifier = Modifier
                                .padding(start = 12.dp)
                        ) {

                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.img_reward),
                    contentDescription = "reward",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(width = 109.dp, height = 112.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun HomeHeaderLoginUser(nickname: String) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {

        val (background, title, untilCount, untilStar, untilText, progress, progressText, star) = createRefs()

        Box(
            modifier = Modifier
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFACEBA),
                        Color(0xFFFACEBA),
                        Color.Transparent
                    )
                ))
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = "${nickname}님과 함께\nDream Away ${getEmoji(0x1F31F)}",
            style = Typography.subtitle1,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 52.dp)
                start.linkTo(parent.start, 23.dp)
            }
        )

        Text(
            text = "21",
            style = Typography.body2,
            color = MainColor,
            modifier = Modifier.constrainAs(untilCount) {
                top.linkTo(title.bottom, 28.dp)
                start.linkTo(title.start)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "star",
            modifier = Modifier
                .size(13.dp)
                .constrainAs(untilStar) {
                    start.linkTo(untilCount.end)
                    top.linkTo(untilCount.top)
                    bottom.linkTo(untilCount.bottom)
                }
        )

        Text(
            text = "until Gold Level",
            style = Typography.body2,
            color = MainColor,
            modifier = Modifier.constrainAs(untilText) {
                start.linkTo(untilStar.end, 3.dp)
                top.linkTo(untilStar.top)
                bottom.linkTo(untilStar.bottom)
            }
        )

        Progressbar(
            current = 9,
            max = 30,
            modifier = Modifier.constrainAs(progress) {
                top.linkTo(untilCount.bottom, 8.dp)
                start.linkTo(untilCount.start)
                end.linkTo(progressText.start, 27.dp)
                bottom.linkTo(parent.bottom, 4.dp)
                width = Dimension.fillToConstraints
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "star",
            modifier = Modifier.constrainAs(star) {
                bottom.linkTo(progressText.bottom, 10.dp)
                end.linkTo(parent.end, 23.dp)
            }
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Black,
//                        fontWeight = FontWeight.ExtraBold
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
                append("30")
            },
            style = Typography.body1,
            color = MainColor,
            modifier = Modifier.constrainAs(progressText) {
                end.linkTo(star.start, 2.dp)
                bottom.linkTo(parent.bottom, (-6).dp)
            }
        )
    }
}

@Composable
fun Progressbar(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    val isStart = remember { mutableStateOf(false) }
    val progress = animateFloatAsState(
        targetValue = if (isStart.value) current.toFloat() / max else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        isStart.value = true
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(8.dp)
            .background(LightGray)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .height(8.dp)
                .fillMaxWidth(progress.value)
                .background(MainColor)
        )
    }
}

@Composable
fun HomeMenuItem(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(125.dp)
                .background(Color(0xFF1E3932))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_order),
                contentDescription = "menu",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "디카페인 카페 아메리카노",
            style = Typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(120.dp)
        )
    }
}