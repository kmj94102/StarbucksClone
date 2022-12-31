package com.example.starbucksclone.view.main.order

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.database.entity.OrderMenuEntity
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getEmoji
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.CustomTabRow
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

/** Order 화면 **/
@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun OrderScreen(
    routeAction: RouteAction,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            stickyHeader {
                /** 해더 영역 **/
                OrderHeader(
                    isExpand = state.firstVisibleItemIndex < 1,
                    routeAction = routeAction,
                    pagerState = pagerState
                )
            }
            item {
                /** 바디 영역 **/
                HorizontalPager(
                    count = 2,
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) {
                    when (it) {
                        0 -> {
                            /** 전체 메뉴 **/
                            AllMenuContainer(
                                routeAction = routeAction,
                                viewModel = viewModel
                            )
                        }
                        1 -> {
                            /** 나만의 메뉴 **/
                            MyMenuContainer(viewModel.myMenuList)
                        }
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .background(DarkBrown)
                .nonRippleClickable {
                    routeAction.goToScreen(RouteAction.Cart)
                }
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(end = 20.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_shopping_basket),
                    contentDescription = "cart",
                )
                Text(
                    text = "${viewModel.cartCount.value}",
                    style = getTextStyle(14, true, White),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 8.dp, bottom = 3.dp)
                )
            }
        }
    }
}

/** 해더 영역 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OrderHeader(
    isExpand: Boolean,
    routeAction: RouteAction,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    val tabItems = listOf("전체 메뉴", "나만의 메뉴")

    Surface(color = White, shadowElevation = 6.dp, modifier = Modifier.padding(bottom = 6.dp)) {
        Column {
            MainTitle(
                titleText = "Order",
                leftIconRes = null,
                rightContents = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 17.dp)
                            .nonRippleClickable {
                                routeAction.goToScreen(RouteAction.MenuSearch)
                            }
                    )
                },
                isExpand = isExpand,
                isShadowVisible = false,
                modifier = Modifier.fillMaxWidth()
            )
            /** [전체 메뉴 / 나만의 메뉴 / 홀 케이크 예약] 탭 영역 **/
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomTabRow(
                    pagerState = pagerState,
                    tabItems = tabItems,
                    coroutineScope = scope,
                    isShadowVisible = false,
                    modifier = Modifier.weight(2f)
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .nonRippleClickable { }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cake),
                        contentDescription = "cake"
                    )
                    Text(text = "홀케이크 예약", style = getTextStyle(size = 14, color = MainColor))
                }
            }
        }
    }
}

/** 전체 메뉴 **/
@Composable
fun AllMenuContainer(
    routeAction: RouteAction,
    viewModel: OrderViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(24.dp))
            listOf("음료", "푸드", "상품").forEach {
                Text(
                    text = it,
                    style = getTextStyle(
                        size = 14,
                        isBold = it == viewModel.selectState.value,
                        color = if (it == viewModel.selectState.value) Black else DarkGray
                    ),
                    modifier = Modifier
                        .nonRippleClickable {
                            viewModel.event(OrderEvent.SelectChange(it))
                        }
                        .padding(horizontal = 14.dp, vertical = 17.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
        Spacer(modifier = Modifier.height(30.dp))
        viewModel.list.forEach {
            AllMenuItem(it) { group, name ->
                routeAction.goToMenuList(group, name)
            }
        }
    }
}

/** 전체 메뉴 선택 아이템 **/
@Composable
fun AllMenuItem(
    menu: OrderMenuEntity,
    onClick: (String, String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 23.dp, bottom = 23.dp)
            .nonRippleClickable {
                onClick(menu.group, menu.name)
            }
    ) {
        CircleImage(imageURL = menu.image)
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = menu.name, style = getTextStyle(16, true, Black))
            if (menu.nameEng.isNotEmpty()) {
                Text(
                    text = menu.nameEng,
                    style = getTextStyle(12, false, DarkGray),
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

/** 나만의 메뉴 **/
@Composable
fun MyMenuContainer(
    myMenuList: List<MyMenuEntity>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (myMenuList.isEmpty()) {
            Text(
                text = "등록된 나만의 메뉴가 없습니다.",
                style = getTextStyle(20),
                modifier = Modifier.padding(top = 20.dp, start = 23.dp, end = 23.dp)
            )
            Text(
                text = "좋아하는 메뉴에 ${getEmoji(0x1F49A)}를 누르고 편리하게 주문해 보세요. 등록된 나만의 메뉴는 HOME 화면에서도 바로 주문하실 수 있습니다.",
                style = getTextStyle(14, false, DarkGray),
                modifier = Modifier.padding(top = 10.dp, start = 23.dp, end = 23.dp)
            )
        } else {
            myMenuList.forEach {
                MyMenuItem(it)
            }
        }
    }
}

/** 나만의 메뉴 선택 아이템 **/
@Composable
fun MyMenuItem(
    myMenu: MyMenuEntity
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CircleImage(
                imageURL = myMenu.image,
                size = 96.dp,
                modifier = Modifier.padding(top = 27.dp, start = 23.dp)
            )
            Column(modifier = Modifier.padding(top = 45.dp, start = 15.dp, end = 23.dp)) {
                Text(
                    text = myMenu.anotherName.ifEmpty { myMenu.name },
                    style = getTextStyle(14, true, Black)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = myMenu.nameEng, style = getTextStyle(12, false, DarkGray))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = 4500.priceFormat(), style = getTextStyle(14, true, Black))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = myMenu.property, style = getTextStyle(12, false, DarkGray))
                Spacer(modifier = Modifier.height(22.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    RoundedButton(text = "담기", isOutline = true, textColor = MainColor) {

                    }
                    Spacer(modifier = Modifier.width(11.dp))
                    RoundedButton(text = "주문하기") {

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
    }
}