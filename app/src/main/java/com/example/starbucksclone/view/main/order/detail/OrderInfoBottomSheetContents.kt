package com.example.starbucksclone.view.main.order.detail

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.common.SegmentButton

@Composable
fun OrderInfoBottomSheetContents(
    id: String,
    info: MenuDetailInfo,
    isHot: Boolean,
    heartClickListener: (MyMenuEntity) -> Unit,
    cartClickListener: () -> Unit,
    orderClickListener: () -> Unit
) {
    val isShow = remember {
        mutableStateOf(false)
    }
    val amount = remember {
        mutableStateOf(1)
    }
    val cupSelected = remember {
        mutableStateOf("매장컵")
    }
    val cupSizeSelected = remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.95f)
    ) {
        OrderInfoHeader(info = info)
        OrderInfoBody(
            info = info,
            cupSelected = cupSelected,
            cupSizeSelected = cupSizeSelected,
            modifier = Modifier.weight(1f)
        )
        OrderInfoFooter(
            price = info.sizePrices.getOrElse(cupSizeSelected.value) { 0 },
            amount = amount,
            heartClickListener = {
                isShow.value = true
            },
            cartClickListener = {

            },
            orderClickListener = {

            }
        )
    }

    MyMenuRegisterDialog(
        isShow = isShow.value,
        name = info.name,
        property = getProperty(isHot, info.sizes.getOrElse(cupSizeSelected.value) { "" }, cupSelected.value),
        okClickListener = {
            isShow.value = false
            heartClickListener(
                MyMenuEntity(
                    id = id,
                    menuIndex = info.index,
                    name = info.name,
                    nameEng = info.nameEng,
                    image = info.image,
                    anotherName = it,
                    price = info.sizePrices.getOrElse(cupSizeSelected.value) { 0 }.toInt(),
                    property = getProperty(isHot, info.sizes.getOrElse(cupSizeSelected.value) { "" }, cupSelected.value),
                    date = System.currentTimeMillis()
                )
            )
        },
        cancelClickListener = {
            isShow.value = false
        }
    )
}

private fun getProperty(isHot: Boolean, size: String, cupSelected: String) =
    "${if (isHot) "HOT" else "ICED"} | $size | $cupSelected"

@Composable
private fun OrderInfoHeader(
    info: MenuDetailInfo,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .size(67.dp, 4.dp)
                .background(Color(0xFF7F7F7F))
        )
        Text(
            text = info.name,
            style = getTextStyle(16, true, Black),
            modifier = Modifier.padding(top = 34.dp, start = 23.dp, end = 23.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(23.dp)
                .background(Color(0xFFF3FAF7))
        ) {
            Text(
                text = "환경을 위해 일회용컵 사용 줄이기에 동참해 주세요",
                style = getTextStyle(12, false, MainColor),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun OrderInfoBody(
    info: MenuDetailInfo,
    cupSelected: MutableState<String>,
    cupSizeSelected: MutableState<Int>,
    modifier: Modifier = Modifier
) {
    val cupList = listOf("매장컵", "개인컵", "일회용컵")
    var visibleState by remember {
        mutableStateOf(cupSelected.value != "매장컵")
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            Text(
                text = "사이즈",
                style = getTextStyle(16, true, Black),
                modifier = Modifier.padding(top = 8.dp, bottom = 13.dp, start = 23.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(start = 23.dp, end = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    info.sizes.forEachIndexed { index, size ->
                        CupSelector(
                            size = size,
                            index = index,
                            isSelected = cupSizeSelected.value == index
                        ) { currentIndex ->
                            cupSizeSelected.value = currentIndex
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }

            Text(
                text = "컵 선택",
                style = getTextStyle(16, true, Black),
                modifier = Modifier.padding(top = 41.dp, bottom = 11.dp, start = 23.dp)
            )
            SegmentButton(
                contentList = cupList,
                selectedValue = cupSelected.value,
                selectedChangeListener = {
                    cupSelected.value = it
                    visibleState = cupSelected.value != "매장컵"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp)
            )
            Spacer(modifier = Modifier.height(23.dp))

            AnimatedVisibility(
                visible = visibleState,
                enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter),
                exit = shrinkOut(shrinkTowards = Alignment.BottomCenter) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 23.dp)
                        .background(LightGray)
                ) {
                    Text(
                        text = when (cupSelected.value) {
                            "개인컵" -> stringResource(id = R.string.individual_cup_guide)
                            "일회용컵" -> stringResource(id = R.string.disposable_cup_guide)
                            else -> ""
                        },
                        lineHeight = 20.sp,
                        style = getTextStyle(12, true, DarkGray),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CupSelector(
    size: String,
    isSelected: Boolean,
    index: Int,
    selectedChangeListener: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MainColor else BorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .nonRippleClickable {
                selectedChangeListener(index)
            }
    ) {
        val imageSize = getCupImageSize(size)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .padding(top = 17.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cup),
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(start = 17.dp, end = 20.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MainColor)
                )
            }
        }

        Text(
            text = size.getCupSize(),
            style = getTextStyle(12),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 10.dp)
        )
    }
}

private fun getCupImageSize(size: String) = when (size) {
    "Short" -> 27.dp
    "Tall" -> 34.dp
    "Grande" -> 40.dp
    "Venti" -> 47.dp
    else -> 27.dp
}

@Composable
private fun OrderInfoFooter(
    price: Long,
    amount: MutableState<Int>,
    modifier: Modifier = Modifier,
    heartClickListener: () -> Unit,
    cartClickListener: () -> Unit,
    orderClickListener: () -> Unit
) {

    Surface(
        elevation = 6.dp,
        color = White,
        modifier = modifier
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (minus, plus, count, priceText, heart, cart, order) = createRefs()

            Icon(
                painter = painterResource(id = R.drawable.ic_minus_circle),
                contentDescription = "minus",
                tint = if (amount.value <= 1) Gray else Color(0xFF585858),
                modifier = Modifier
                    .constrainAs(minus) {
                        top.linkTo(count.top)
                        bottom.linkTo(priceText.bottom)
                        start.linkTo(parent.start, 20.dp)
                    }
                    .nonRippleClickable {
                        if (amount.value > 1) {
                            amount.value = amount.value - 1
                        }
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_plus_circle),
                contentDescription = "plus",
                tint = if (amount.value > 100) Gray else Color(0xFF585858),
                modifier = Modifier
                    .constrainAs(plus) {
                        top.linkTo(count.top)
                        bottom.linkTo(count.bottom)
                        start.linkTo(count.end)
                    }
                    .nonRippleClickable {
                        if (amount.value < 100) {
                            amount.value = amount.value + 1
                        }
                    }
            )
            Text(
                text = "${amount.value}",
                style = getTextStyle(20),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(count) {
                        top.linkTo(parent.top, 17.dp)
                        start.linkTo(minus.end)
                        end.linkTo(plus.start)
                        width = Dimension.value(55.dp)
                    }
            )

            Text(
                text = (price * amount.value).toPriceFormat(),
                style = getTextStyle(20, true),
                modifier = Modifier.constrainAs(priceText) {
                    top.linkTo(parent.top, 22.dp)
                    end.linkTo(parent.end, 20.dp)
                }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_empty_heart),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(heart) {
                        bottom.linkTo(parent.bottom, 18.dp)
                        start.linkTo(parent.start, 23.dp)
                    }
                    .nonRippleClickable { heartClickListener() }
            )

            RoundedButton(
                text = "담기",
                isOutline = true,
                textColor = MainColor,
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(cart) {
                        top.linkTo(order.top)
                        bottom.linkTo(order.bottom)
                        end.linkTo(order.start, 12.dp)
                    }
                    .nonRippleClickable { cartClickListener() }
            )

            RoundedButton(
                text = "주문하기",
                onClick = {
                    orderClickListener()
                },
                modifier = Modifier.constrainAs(order) {
                    top.linkTo(priceText.bottom, 15.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, 20.dp)
                }
            )
        }
    }
}