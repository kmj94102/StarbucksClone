package com.example.starbucksclone.view.main.order.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun CartScreen(
    routeAction: RouteAction,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        /** 해더 영역 **/
        CartHeader(
            routeAction = routeAction,
            isExpand = state.isScrolled.not(),
            allDeleteListener = {
                viewModel.event(CartEvent.AllDeleteCartItems)
            }
        )
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                if (viewModel.list.isEmpty()) {
                    /** 장바구니가 비어있을 때 화면 **/
                    CartEmptyBody()
                } else {
                    /** 장바구니 리스트 **/
                    viewModel.list.forEachIndexed { index, cartItem ->
                        CartItem(
                            cartEntity = cartItem,
                            amountChangeListener = {
                                viewModel.event(CartEvent.AmountChange(value = it, index = index))
                            },
                            deleteListener = {
                                viewModel.event(CartEvent.DeleteCartItem(it))
                            }
                        )
                    }
                }
            }
        }
        if (viewModel.list.isNotEmpty()) {
            CartFooter(
                routeAction = routeAction,
                cartItems = viewModel.list
            )
        }
    }
}

@Composable
fun CartHeader(
    routeAction: RouteAction,
    isExpand: Boolean,
    allDeleteListener: () -> Unit
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
                        allDeleteListener()
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
fun CartItem(
    cartEntity: CartEntity,
    amountChangeListener: (Int) -> Unit,
    deleteListener: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (image, name, nameEng, property, price, amount,
            totalPrice, minus, plus, delete, line) = createRefs()

        CircleImage(
            imageURL = cartEntity.image,
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top, 30.dp)
                start.linkTo(parent.start, 23.dp)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_circle_cancel),
            contentDescription = "delete",
            modifier = Modifier
                .constrainAs(delete) {
                    top.linkTo(parent.top, 21.dp)
                    end.linkTo(parent.end, 21.dp)
                }
                .nonRippleClickable {
                    deleteListener(cartEntity.index)
                }
        )

        Text(
            text = cartEntity.name,
            style = getTextStyle(14),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top, 47.dp)
                start.linkTo(image.end, 15.dp)
                end.linkTo(parent.end, 21.dp)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = cartEntity.nameEng,
            style = getTextStyle(12, false, BorderColor),
            modifier = Modifier.constrainAs(nameEng) {
                top.linkTo(name.bottom, 5.dp)
                start.linkTo(name.start)
                end.linkTo(parent.end, 21.dp)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = cartEntity.property,
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.constrainAs(property) {
                top.linkTo(nameEng.bottom, 13.dp)
                start.linkTo(name.start)
                end.linkTo(parent.end, 21.dp)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = cartEntity.price.priceFormat(),
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.constrainAs(price) {
                top.linkTo(property.top)
                end.linkTo(parent.end, 22.dp)
                end.linkTo(parent.end, 21.dp)
                width = Dimension.fillToConstraints
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_minus_circle),
            contentDescription = "minus",
            tint = if (cartEntity.amount <= 1) Gray else Color(0xFF585858),
            modifier = Modifier
                .constrainAs(minus) {
                    start.linkTo(name.start)
                    top.linkTo(property.bottom, 21.dp)
                }
                .nonRippleClickable {
                    if (cartEntity.amount > 1) {
                        amountChangeListener(-1)
                    }
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_plus_circle),
            contentDescription = "plus",
            tint = if (cartEntity.amount > 100) Gray else Color(0xFF585858),
            modifier = Modifier
                .constrainAs(plus) {
                    start.linkTo(minus.end, 44.dp)
                    top.linkTo(minus.top)
                }
                .nonRippleClickable {
                    if (cartEntity.amount < 100) {
                        amountChangeListener(1)
                    }
                }
        )

        Text(
            text = "${cartEntity.amount}",
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
            text = (cartEntity.amount * cartEntity.price).priceFormat(),
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

@Composable
fun CartFooter(
    routeAction: RouteAction,
    cartItems: List<CartEntity>
) {
    val totalPrice = cartItems
        .map { it.price * it.amount }
        .reduceOrNull { acc, i -> acc + i }
        ?.priceFormat()
        ?: 0.priceFormat()

    Surface(
        elevation = 6.dp,
        color = White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "총 ${cartItems.size}건",
                    style = getTextStyle(12, true, Brown),
                    modifier = Modifier.padding(start = 23.dp, top = 21.dp)
                )
                Text(
                    text = totalPrice,
                    textAlign = TextAlign.End,
                    style = getTextStyle(24, true, Black),
                    modifier = Modifier
                        .padding(top = 20.dp, end = 23.dp)
                        .weight(1f)
                )
            }
            RoundedButton(
                text = "주문하기",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp, vertical = 13.dp)
            ) {
                routeAction.goToScreen(RouteAction.Payment)
            }
        }
    }
}