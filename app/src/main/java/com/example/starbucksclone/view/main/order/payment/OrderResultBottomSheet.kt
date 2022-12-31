package com.example.starbucksclone.view.main.order.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.util.getEmoji
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.Progressbar
import kotlinx.coroutines.delay

@Composable
fun OrderResultBottomSheet(
    cartList: List<CartEntity>
) {
    val state = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(true) {
        delay(1000)
        state.value = 1
        delay(1000)
        state.value = 2
        delay(1000)
        state.value = 3
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.95f)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(67.dp, 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(DarkGray)
                .align(Alignment.CenterHorizontally)
        )
        OrderResultHeader(state = state.value)
        Spacer(modifier = Modifier.height(13.dp))
        OrderResultProgress(state = state.value)
        Spacer(modifier = Modifier.height(30.dp))
        OrderResultBody(cartList = cartList, modifier = Modifier.weight(1f))
    }
}

@Composable
fun OrderResultHeader(state: Int) {
    var title = ""
    var comment = ""
    when (state) {
        0 -> {
            title = "민재님, 결제가 완료되었습니다."
            comment = "결제 정보를 해당 매장에 전달하고 있어요."
        }
        1 -> {
            title = "주문을 확인하고 있습니다${getEmoji(0x1F3C3)}"
            comment = "주문 승인 즉시 메뉴 준비가 시작됩니다. 완성 후, 빠르게 픽업해주세요."
        }
        2 -> {
            title = "주문이 확인되었습니다${getEmoji(0x1F44C)}"
            comment = "메뉴를 만들고 있어요. 조금만 기다려주세요."
        }
        3 -> {
            title = "민재님, 메뉴가 모두 준비되었어요.${getEmoji(0x1F973)}"
            comment = "메뉴가 모두 준비되었어요. 픽업대에서 메뉴를 픽업해주세요!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 23.dp, end = 23.dp, top = 90.dp)
            .heightIn(min = 122.dp)
    ) {
        Text(text = title, style = getTextStyle(24, true, Black))
        Text(
            text = comment,
            style = getTextStyle(14, false, DarkGray),
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

@Composable
fun OrderResultProgress(state: Int) {
    val process = when (state) {
        1 -> {
            50
        }
        2 -> {
            75
        }
        3 -> {
            100
        }
        else -> {
            10
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "결제 완료",
                textAlign = TextAlign.Center,
                style = getTextStyle(12, false, if (state == 0) Black else DarkGray),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "주문 요청",
                textAlign = TextAlign.Center,
                style = getTextStyle(12, false, if (state == 0) Black else DarkGray),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "주문 승인",
                textAlign = TextAlign.Center,
                style = getTextStyle(12, false, if (state == 0) Black else DarkGray),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "준비 완료",
                textAlign = TextAlign.Center,
                style = getTextStyle(12, false, if (state == 0) Black else DarkGray),
                modifier = Modifier.weight(1f)
            )
        }
        Progressbar(
            current = process,
            max = 100,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        )
    }
}

@Composable
fun OrderResultBody(
    cartList: List<CartEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LightGray)
    ) {
        Text(
            text = "주문 내역(${cartList.size})",
            style = getTextStyle(16, true, Black),
            modifier = Modifier.padding(top = 30.dp, start = 23.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                cartList.forEachIndexed { index, info ->
                    OrderResultItem(info)
                    if (index < cartList.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 23.dp)
                                .height(1.dp)
                                .background(Gray)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderResultItem(info: CartEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        CircleImage(
            imageURL = info.image,
            size = 77.dp
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = info.name,
                style = getTextStyle(14),
                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
            )
            Text(text = info.property, style = getTextStyle(12, false, DarkGray))
        }
    }
}