package com.example.starbucksclone.view.main.order.cart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartScreen(
    routeAction: RouteAction
) {
    val state = rememberLazyListState()
    val list = listOf<String>("", "", "", "")

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        stickyHeader {
            CartHeader(
                routeAction = routeAction,
                isExpand = state.firstVisibleItemIndex < 1
            )
        }
        item {
            if (list.isEmpty()) {
                CartEmptyBody()
            } else {
                CartBody(list)
            }
        }
    }
}

@Composable
fun CartHeader(
    routeAction: RouteAction,
    isExpand: Boolean
) {
    MainTitle(
        titleText = "장바구니",
        titleColor = White,
        isExpand = isExpand,
        leftIconTint = White,
        onLeftIconClick = {
            routeAction.popupBackStack()
        },
        rightContents = {
            Icon(
                painter = painterResource(id = R.drawable.ic_recycle_bin),
                contentDescription = "recycle bin",
                tint = White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 17.dp)
                    .nonRippleClickable {

                    }
            )
        },
        backgroundColor = DarkBrown,
        modifier = Modifier
            .fillMaxWidth()
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(DarkBrown)
    )
}

@Composable
fun CartEmptyBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Text(
            text = "장바구니가 비어있습니다.",
            style = getTextStyle(20, true),
            modifier = Modifier.padding(top = 68.dp)
        )
        Text(
            text = "원하는 메뉴를 장바구니에 담고\n한번에 주문해 보세요.",
            style = getTextStyle(14),
            modifier = Modifier.padding(top = 10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.img_cart),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(276.dp, 344.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun CartBody(
    list: List<String>
) {
    list.forEach { _ ->
        CartItem()
    }
}

@Composable
fun CartItem() {
    val itemAmount = remember {
        mutableStateOf(1)
    }
    val itemPrice = 4400

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (image, name, nameEng, property, price, amount,
            totalPrice, minus, plus, delete, line) = createRefs()

        CircleImage(
            imageURL = "https://image.istarbucks.co.kr/cardImg/20220712/009226_WEB.png",
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top, 30.dp)
                start.linkTo(parent.start, 23.dp)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_circle_cancel),
            contentDescription = "delete",
            modifier = Modifier.constrainAs(delete) {
                top.linkTo(parent.top, 21.dp)
                end.linkTo(parent.end, 21.dp)
            }
        )

        Text(
            text = "디카페인 카페 아메리카노",
            style = getTextStyle(14),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top, 47.dp)
                start.linkTo(image.end, 15.dp)
            }
        )
        Text(
            text = "DECAF Caffe Americano",
            style = getTextStyle(12, false, BorderColor),
            modifier = Modifier.constrainAs(nameEng) {
                top.linkTo(name.bottom, 5.dp)
                start.linkTo(name.start)
            }
        )

        Text(
            text = "HOT | Tall | 개인컵",
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.constrainAs(property) {
                top.linkTo(nameEng.bottom, 13.dp)
                start.linkTo(name.start)
            }
        )

        Text(
            text = itemPrice.priceFormat(),
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.constrainAs(price) {
                top.linkTo(property.top)
                end.linkTo(parent.end, 22.dp)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_minus_circle),
            contentDescription = "minus",
            tint = if (itemAmount.value <= 1) Gray else Color(0xFF585858),
            modifier = Modifier.constrainAs(minus) {
                start.linkTo(name.start)
                top.linkTo(property.bottom, 21.dp)
            }
                .nonRippleClickable {
                    if (itemAmount.value > 1) {
                        itemAmount.value = itemAmount.value - 1
                    }
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_plus_circle),
            contentDescription = "plus",
            tint = if (itemAmount.value > 100) Gray else Color(0xFF585858),
            modifier = Modifier
                .constrainAs(plus) {
                    start.linkTo(minus.end, 44.dp)
                    top.linkTo(minus.top)
                }
                .nonRippleClickable {
                    if (itemAmount.value < 100) {
                        itemAmount.value = itemAmount.value + 1
                    }
                }
        )

        Text(
            text = "${itemAmount.value}",
            textAlign = TextAlign.Center,
            style = getTextStyle(14),
            modifier = Modifier.constrainAs(amount) {
                top.linkTo(minus.top)
                bottom.linkTo(minus.bottom)
                start.linkTo(minus.end)
                end.linkTo(plus.start)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = (itemAmount.value * itemPrice).priceFormat(),
            style = getTextStyle(14),
            modifier = Modifier.constrainAs(totalPrice) {
                top.linkTo(plus.top)
                bottom.linkTo(plus.bottom)
                end.linkTo(parent.end, 22.dp)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
                .constrainAs(line) {
                    top.linkTo(totalPrice.bottom, 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

    }
}