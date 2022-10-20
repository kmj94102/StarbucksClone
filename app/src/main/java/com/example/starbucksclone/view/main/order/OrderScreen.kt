package com.example.starbucksclone.view.main.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.MotionTitle
import com.example.starbucksclone.view.common.RoundedButton
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OrderScreen() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, tab, pager, info) = createRefs()
        val lazyListState = LazyListState()
        val pagerState = rememberPagerState()

        /** 타이틀 영역 **/
        OderTitle(
            lazyListState = lazyListState,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        /** 텝 영역 **/
        OderTab(
            pagerState = pagerState,
            modifier = Modifier.constrainAs(tab) {
                top.linkTo(title.bottom, 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        /** 뷰페이져 **/
        OrderViewPager(
            pagerState = pagerState,
            modifier = Modifier.constrainAs(pager) {
                top.linkTo(tab.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /** 주문 정보 **/
        OrderInfo(
            modifier = Modifier.constrainAs(info) {
                bottom.linkTo(pager.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

/** 타이틀 영역 **/
@Composable
fun OderTitle(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        MotionTitle(
            leftIconRes = null,
            titleText = "Order",
            lazyListSate = lazyListState
        )

        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "search",
            modifier = Modifier
                .padding(top = 7.dp, end = 21.dp)
                .align(Alignment.TopEnd)
                .nonRippleClickable { }
        )
    }
}

/** 텝 영역 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OderTab(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val tabItems = listOf("전체 메뉴", "나만의 메뉴")
    val coroutineScope = rememberCoroutineScope()

    Surface(
        shadowElevation = 6.dp,
        modifier = modifier
            .background(White)
            .padding(bottom = 6.dp)
            .drawWithContent {
                val paddingPx = with(density) { 6.dp.toPx() }
                clipRect(
                    left = -paddingPx,
                    top = 0f,
                    right = size.width + paddingPx,
                    bottom = size.height + paddingPx
                ) {
                    this@drawWithContent.drawContent()
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(White)
        ) {
            androidx.compose.material.TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = White,
                indicator = {
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, it),
                        color = MainColor,
                    )
                },
                divider = {},
                modifier = Modifier.weight(1f)
            ) {
                tabItems.forEachIndexed { index, value ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        unselectedContentColor = DarkGray,
                        selectedContentColor = Black
                    ) {
                        Text(
                            text = value,
                            style = Typography.body2,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 14.dp)
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_cake),
                contentDescription = "cake",
                modifier.padding(start = 30.dp)
            )

            Text(
                text = "홀케이크 예약",
                fontSize = 14.sp,
                color = MainColor,
                modifier = Modifier.padding(end = 23.dp)
            )
        }
    }
}

/** 뷰페이져 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OrderViewPager(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        count = 2,
        state = pagerState,
        modifier = modifier
    ) {
        when (it) {
            0 -> {
                AllMenu()
            }
            1 -> {
                MyMenu()
            }
        }
    }
}

/** 전체 메뉴 **/
@Composable
fun AllMenu(modifier: Modifier = Modifier) {
    val select = remember { mutableStateOf(0) }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(start = 24.dp)) {
            MenuGroupItem(
                isSelected = select.value == 0,
                text = "음료"
            ) {
                select.value = 0
            }

            MenuGroupItem(
                isSelected = select.value == 1,
                text = "푸드"
            ) {
                select.value = 1
            }

            MenuGroupItem(
                isSelected = select.value == 2,
                text = "상품"
            ) {
                select.value = 2
            }
        }


        Divider()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 28.dp, bottom = (57 + 28).dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AllMenuItem(
                    title = "New",
                    caption = ""
                )
            }

            item {
                AllMenuItem(
                    title = "추천",
                    caption = "Recommend"
                )
            }

            item {
                AllMenuItem(
                    title = "콜드 브루",
                    caption = "Cold Brew"
                )
            }

            item {
                AllMenuItem(
                    title = "에스프레소",
                    caption = "Espresso"
                )
            }

            item {
                AllMenuItem(
                    title = "New",
                    caption = ""
                )
            }

            item {
                AllMenuItem(
                    title = "New",
                    caption = ""
                )
            }
        }
    }
}

/** 메뉴 그룹 선택 아이템 **/
@Composable
fun MenuGroupItem(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) Black else DarkGray,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 17.dp)
    )
}

/** 전체 메뉴 아이템 **/
@Composable
fun AllMenuItem(
    title: String,
    caption: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(96.dp)
                .background(MainColor)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = title, style = Typography.body2, color = Black)
            caption?.let {
                Text(
                    text = it,
                    style = Typography.caption,
                    color = DarkGray,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }

    }
}

/** 나만의 메뉴 **/
@Composable
fun MyMenu(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 57.dp),
        modifier = modifier.fillMaxSize()
    ) {
        (0..4).forEach { _ ->
            item {
                MyMenuItem(
                    name = "콜드 브루",
                    nameEng = "Cold Brew",
                    price = "4,500 원",
                    info = "ICED | Tall | 일회용 컵"
                )
            }
        }
    }
}

/** 나만의 메뉴 아이템 **/
@Composable
fun MyMenuItem(
    name: String,
    nameEng: String,
    price: String,
    info: String
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (image, _name, _nameEng, _price, _info, heart, add, order) = createRefs()

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MainColor)
                .constrainAs(image) {
                    top.linkTo(parent.top, 27.dp)
                    start.linkTo(parent.start, 23.dp)
                }
        )

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            color = Black,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(_name) {
                top.linkTo(image.top, 16.dp)
                start.linkTo(image.end, 15.dp)
            }
        )

        Text(
            text = nameEng,
            style = Typography.caption,
            color = DarkGray,
            modifier = Modifier.constrainAs(_nameEng) {
                top.linkTo(_name.bottom, 4.dp)
                start.linkTo(_name.start)
            }
        )

        Text(
            text = price,
            fontWeight = FontWeight.Bold,
            color = Black,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(_price) {
                top.linkTo(_nameEng.bottom, 10.dp)
                start.linkTo(_nameEng.start)
            }
        )

        Text(
            text = info,
            style = Typography.caption,
            color = DarkGray,
            modifier = Modifier.constrainAs(_info) {
                top.linkTo(_price.bottom, 12.dp)
                start.linkTo(_price.start)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = "heart",
            modifier = Modifier.constrainAs(heart) {
                top.linkTo(add.top)
                bottom.linkTo(add.bottom)
                end.linkTo(add.start, 14.dp)
            }
        )

        RoundedButton(
            text = "담기",
            isOutline = true,
            textColor = MainColor,
            horizontalPadding = 22.dp,
            verticalPadding = 5.dp,
            modifier = Modifier.constrainAs(add) {
                top.linkTo(order.top)
                end.linkTo(order.start, 10.dp)
            }
        ) {

        }

        RoundedButton(
            text = "주문하기",
            horizontalPadding = 22.dp,
            verticalPadding = 5.dp,
            modifier = Modifier.constrainAs(order) {
                top.linkTo(_info.bottom, 22.dp)
                bottom.linkTo(parent.bottom, 30.dp)
                end.linkTo(parent.end, 23.dp)
            }
        ) {

        }

    }
}

/** 주문 정보 **/
@Composable
fun OrderInfo(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.background(DarkBrown)
    ) {
        val (text, arrowIcon, basketIcon, divider, count) = createRefs()

        Text(
            text = "주문할 매장을 선택해주세요",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start, 23.dp)
                end.linkTo(basketIcon.start, 60.dp)
                top.linkTo(parent.top, 18.dp)
                width = Dimension.fillToConstraints
            }
        )

        Box(modifier = Modifier
            .height(1.dp)
            .background(Color(0xFF444341))
            .constrainAs(divider) {
                top.linkTo(text.bottom, 5.dp)
                start.linkTo(text.start)
                end.linkTo(text.end)
                width = Dimension.fillToConstraints
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = "down",
            tint = White,
            modifier = Modifier.constrainAs(arrowIcon) {
                top.linkTo(text.top)
                bottom.linkTo(text.bottom)
                end.linkTo(text.end)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_shopping_basket),
            contentDescription = "shopping basket",
            modifier = Modifier.constrainAs(basketIcon) {
                top.linkTo(parent.top, 15.dp)
                bottom.linkTo(parent.bottom, 15.dp)
                end.linkTo(parent.end, 20.dp)
            }
        )

        Text(
            text = "0",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.constrainAs(count) {
                start.linkTo(basketIcon.start)
                end.linkTo(basketIcon.end)
                top.linkTo(basketIcon.top, 8.dp)
                bottom.linkTo(basketIcon.bottom, 3.dp)
            }
        )

    }
}