package com.example.starbucksclone.view.main.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.HomeNewMenu
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getEmoji
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.Progressbar
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    routeAction: RouteAction,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            if (viewModel.isLogin.value) {
                /** 로그인 한 유저 화면 **/
                item {
                    UserHomeInfo(routeAction, viewModel.nickname.value) {
                        viewModel.event(HomeEvent.Logout)
                    }
                }
            } else {
                /** 로그인을 하지 않은 유저 화면 **/
                item { GuestHomeInfoContainer(routeAction) }
            }

            /** What's New 스크롤 후 고정 영역 **/
            stickyHeader {
                WhatsNewContainer(isScrolled = state.firstVisibleItemIndex > 1)
            }

            /** 새로 나온 메뉴 영역 **/
            item {
                if (viewModel.newMenuList.isNotEmpty()){
                    HomeNewMenuContainer(
                        list = viewModel.newMenuList,
                        onClickListener = { indexes, name ->
                            routeAction.goToMenuDetail(indexes, name)
                        }
                    )
                }
            }

            /** 이미지 베너 영역 **/
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

        /** Delivers Floating 버튼 **/
        DeliversFloatingButton(
            isExpand = state.firstVisibleItemIndex < 1,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 23.dp, bottom = 16.dp)
        )
    }
}

/** What's New 스크롤 후 고정 영역 **/
@Composable
fun WhatsNewContainer(isScrolled: Boolean) {
    Surface(
        color = White,
        elevation = if (isScrolled) 6.dp else 0.dp
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
                text = stringResource(id = R.string.whats_new), modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_bell),
                contentDescription = null
            )
        }
    }
}

/** 로그인을 하지 않은 유저 화면 **/
@Composable
fun GuestHomeInfoContainer(
    routeAction: RouteAction
) {
    Text(
        text = stringResource(id = R.string.hello_starbucks),
        style = getTextStyle(24, true),
        modifier = Modifier.padding(top = 52.dp, start = 23.dp)
    )

    Surface(
        elevation = 6.dp,
        color = White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.home_reward_guide),
                style = getTextStyle(16),
                lineHeight = 24.sp,
                modifier = Modifier.padding(top = 36.dp, start = 30.dp)
            )

            RoundedButton(
                text = stringResource(id = R.string.signup),
                modifier = Modifier.padding(top = 120.dp, start = 30.dp, bottom = 35.dp)
            ) {
                routeAction.goToScreen(RouteAction.Terms)
            }

            RoundedButton(
                text = stringResource(id = R.string.login),
                textColor = MainColor,
                isOutline = true,
                modifier = Modifier.padding(top = 120.dp, start = 130.dp)
            ) {
                routeAction.goToScreen(RouteAction.Login)
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

/** 로그인한 유저 화면 **/
@Composable
fun UserHomeInfo(
    routeAction: RouteAction,
    nickname: String,
    logout: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {

        val (background, title, untilCount, untilStar, untilText, progress, progressText, star) = createRefs()

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFACEBA),
                            Color(0xFFFACEBA),
                            Color.Transparent
                        )
                    )
                )
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = stringResource(id = R.string.home_greeting, nickname, getEmoji(0x1F31F)),
            style = getTextStyle(24, true),
            modifier = Modifier
                .nonRippleClickable { logout() }
                .constrainAs(title) {
                    top.linkTo(parent.top, 52.dp)
                    start.linkTo(parent.start, 23.dp)
                }
        )

        Text(
            text = "21",
            style = getTextStyle(16, true),
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
            text = stringResource(id = R.string.until_gold_level),
            style = getTextStyle(16, true),
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
            style = getTextStyle(24, true),
            color = MainColor,
            modifier = Modifier
                .constrainAs(progressText) {
                    end.linkTo(star.start, 2.dp)
                    bottom.linkTo(parent.bottom, (-6).dp)
                }
                .nonRippleClickable {
                    routeAction.goToScreen(RouteAction.Rewords)
                }
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

/** 새로 나온 메뉴 **/
@Composable
fun HomeNewMenuContainer(
    list: List<HomeNewMenu>,
    onClickListener: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Text(
            text = stringResource(id = R.string.new_menu),
            style = getTextStyle(20, true),
            modifier = Modifier.padding(start = 23.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                list.forEach {
                    HomeNewMenuItem(
                        item = it,
                        modifier = Modifier
                            .nonRippleClickable {
                                onClickListener(it.indexes, it.name)
                            }
                    )
                }
            }
        }
    }
}

/** 새로 나온 메뉴 아이템 **/
@Composable
fun HomeNewMenuItem(
    item: HomeNewMenu,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        CircleImage(
            imageURL = item.image,
            backgroundColor = MainColor,
        )
        Text(
            text = item.name,
            style = getTextStyle(12),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 11.dp)
                .width(110.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

/** Delivers Floating 버튼 **/
@Composable
fun DeliversFloatingButton(
    isExpand: Boolean,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(if (isExpand) 160.dp else 54.dp)
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
            .width(width)
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
                text = stringResource(id = R.string.delivers),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
            )
        }
    }
}