package com.example.starbucksclone.view.main.pay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardEntity
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PayScreen(
    routeAction: RouteAction,
    viewModel: PayViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        /** 해더 영역 **/
        stickyHeader { PayHeader(state.isScrolled.not(), routeAction) }
        /** 바디 영역 **/
        item {
            PayBody(
                routeAction = routeAction,
                viewModel = viewModel,
            )
        }
        /** 풋터 영역 **/
        item { PayFooter() }
    }
}

/** 해더 영역 **/
@Composable
fun PayHeader(
    isExpand: Boolean,
    routeAction: RouteAction
) {
    MainTitle(
        titleText = "Pay",
        isExpand = isExpand,
        leftIconRes = null,
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = "list",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 17.dp)
                    .nonRippleClickable {
                        routeAction.goToScreen(RouteAction.CardList)
                    }
            )
        }
    )
}

/** 바디 영역 **/
@Composable
fun PayBody(
    routeAction: RouteAction,
    viewModel: PayViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (viewModel.cardList.isEmpty()) {
            PayEmptyCardBody(routeAction)
        } else {
            PayCardListBody(
                cardList = viewModel.cardList,
                routeAction = routeAction
            )
        }
    }
}

/** 스타벅스 카드가 있을 때 화면 (리스트) **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PayCardListBody(
    cardList: List<CardEntity>,
    routeAction: RouteAction
) {
    Column(Modifier.fillMaxWidth()) {
        HorizontalPager(
            count = cardList.size,
            contentPadding = PaddingValues(start = 12.dp, end = 26.dp),
            itemSpacing = 12.dp
        ) {
            PayCardItem(card = cardList[it])
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 23.dp, bottom = 30.dp)
        ) {
            Text(
                text = "Coupon",
                textAlign = TextAlign.Center,
                style = getTextStyle(14, true),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 10.dp)
                    .background(Black)
            )
            Text(
                text = "Gift Item",
                textAlign = TextAlign.Center,
                style = getTextStyle(14, true),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/** 스타벅스 카드 리스트 아이템 **/
@Composable
fun PayCardItem(
    card: CardEntity
) {
    Surface(
        elevation = 6.dp,
        color = White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = card.cardImage,
                contentDescription = "card",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = card.cardName, style = getTextStyle(12))
                Image(
                    painter = painterResource(
                        id = if (card.representative) R.drawable.ic_star_circle_selected
                        else R.drawable.ic_star_circle
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(24.dp)
                )
            }
            Text(
                text = card.balance.toPriceFormat(),
                style = getTextStyle(20, true),
                modifier = Modifier.padding(top = 2.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_barcode),
                contentDescription = "barcode",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(top = 14.dp)
                    .size(190.dp, 36.dp)
            )

            Text(
                text = card.cardNumber.toSecretFormat(),
                style = getTextStyle(12),
                modifier = Modifier.padding(top = 7.dp)
            )

            Text(
                text = buildAnnotatedString {
                    append("바코드 유효시간 ")
                    withStyle(
                        style = SpanStyle(color = MainColor)
                    ) {
                        append("09:54")
                    }
                },
                style = getTextStyle(12)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 76.dp, end = 76.dp, top = 20.dp, bottom = 40.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_auto_charging),
                        contentDescription = "auto charging",
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = "자동 충전",
                        style = getTextStyle(size = 12, color = DarkGray),
                        modifier = Modifier.padding(top = 7.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_charging),
                        contentDescription = "normal charging",
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = "일반 충전",
                        style = getTextStyle(size = 12, color = DarkGray),
                        modifier = Modifier.padding(top = 7.dp)
                    )
                }
            }
        }
    }
}

/** 스타벅스 카드가 없을 때 화면 **/
@Composable
fun PayEmptyCardBody(routeAction: RouteAction) {
    Surface(
        elevation = 6.dp,
        color = White,
        modifier = Modifier.padding(horizontal = 11.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .nonRippleClickable {
                    routeAction.goToScreen(RouteAction.CardRegistration)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_card),
                contentDescription = "card registration",
                modifier = Modifier
                    .padding(top = 18.dp)
                    .size(256.dp, 163.dp)
            )
            Text(
                text = "스타벅스 카드를 등록해보세요.",
                style = getTextStyle(size = 20, isBold = true),
                modifier = Modifier.padding(top = 25.dp)
            )
            Text(
                text = "매장과 사이렌오더에서 쉽고 편리하게\n사용할 수 있고, 별도 적립할 수 있습니다.",
                textAlign = TextAlign.Center,
                style = getTextStyle(size = 16, color = DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            )
        }
    }
}

/** 풋터 영역 **/
@Composable
fun PayFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(bottom = 100.dp)
            .fillMaxWidth()
            .background(Color(0xFFFFF7E6))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_banner),
            contentDescription = "banner",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(78.dp)
        )
    }
}