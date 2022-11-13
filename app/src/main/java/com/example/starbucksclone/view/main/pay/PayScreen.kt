package com.example.starbucksclone.view.main.pay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.util.toSecretFormat
import com.example.starbucksclone.view.common.MotionTitle
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PayScreen(
    routAction: RoutAction,
    viewModel: PayViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 30.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        stickyHeader {
            Box(modifier = Modifier.fillMaxWidth()) {
                MotionTitle(
                    leftIconRes = null,
                    lazyListSate = listState,
                    titleText = "Pay"
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = "menu",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 7.dp, end = 17.dp)
                        .nonRippleClickable {
                            routAction.goToCardRegistration()
                        }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            if (viewModel.cardList.isEmpty()) {
                EmptyPayCard {
                    routAction.goToCardRegistration()
                }
            } else {
                PayCardPager(viewModel = viewModel)
            }
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
            CouponAndGift()
            Spacer(modifier = Modifier.height(20.dp))
            Banner()
        }
    }
}

@Composable
fun EmptyPayCard(onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(445.dp)
            .padding(horizontal = 10.dp)
            .nonRippleClickable {
                onClick()
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_card),
                contentDescription = "empty",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(width = 263.dp, height = 167.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "스타벅스 카드를 등록해보세요.",
                style = Typography.subtitle2,
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "매장과 사이렌오더에서 쉽고 편리하게\n사용할 수 있고, 별도 적립할 수 있습니다.",
                style = Typography.body1,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PayCardPager(viewModel: PayViewModel) {
    HorizontalPager(
        count = viewModel.cardList.size,
        contentPadding = PaddingValues(start = 16.dp, end = 26.dp),
        itemSpacing = 10.dp
    ) {
        PayCardItem(
            cardEntity = viewModel.cardList[it],
            validTime = viewModel.validTime.value
        )
    }
}

@Composable
fun PayCardItem(
    cardEntity: CardEntity,
    validTime: String
) {
    Surface(
        shadowElevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(White)
        ) {
            AsyncImage(
                model = cardEntity.cardImage,
                contentDescription = "cardImage",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(178.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = cardEntity.cardName, style = Typography.caption)
                Image(
                    painter = painterResource(
                        if (cardEntity.representative) R.drawable.ic_star_circle_selected
                        else R.drawable.ic_star_circle
                    ),
                    contentDescription = null,
                )
            }

            Text(text = cardEntity.balance.toPriceFormat(), style = Typography.subtitle2)

            Spacer(modifier = Modifier.height(14.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_barcode),
                contentDescription = "barcode",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.size(190.dp, 36.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = cardEntity.cardNumber.toSecretFormat(),
                style = Typography.caption,
                color = Black
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = buildAnnotatedString {
                    append(text = "바코드 유효시간 ")
                    withStyle(
                        style = SpanStyle(
                            color = MainColor
                        )
                    ) {
                        append(validTime)
                    }
                },
                style = Typography.caption,
                color = Black
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_auto_charging),
                        contentDescription = "",
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = "자동 충전", style = Typography.caption)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_charging),
                        contentDescription = "",
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = "일반 충전", style = Typography.caption)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun CouponAndGift() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Coupon",
            style = Typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(width = 1.dp, height = 10.dp)
                .background(Black)
        )
        Text(
            text = "Gift Item",
            style = Typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Banner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFFFFF7E6))
    )
}