package com.example.starbucksclone.view.main.order.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.view.common.CustomCheckBox
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun MethodOfPaymentBottomSheet(
    cardList: List<CardInfo>,
    selectCardNumber: String,
    chargingListener: (String) -> Unit,
    selectChangerListener: (String) -> Unit
) {
    val selectIndex = remember {
        mutableStateOf(cardList.indexOfFirst { it.cardNumber == selectCardNumber })
    }
    var changedCardNumber = selectCardNumber
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

        Text(
            text = "결제 수단",
            style = getTextStyle(16, true),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 33.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 37.dp, start = 20.dp, end = 20.dp)
        ) {
            CustomCheckBox(
                text = "스타벅스 카드",
                textStyle = getTextStyle(14),
                selected = true,
                checkedIcon = R.drawable.ic_circle_checkbox_checked,
                onClick = { },
            )
            Spacer(modifier = Modifier.weight(1f))
            RoundedButton(
                text = "충전하기",
                isOutline = true,
                textColor = MainColor
            ) {
                chargingListener(
                    cardList.getOrNull(index = selectIndex.value)?.cardNumber ?: ""
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp),
            modifier = Modifier
                .padding(top = 14.dp)
                .background(LightGray)
        ) {
            item {
                cardList.forEachIndexed { index, info ->
                    MethodOfPaymentCardItem(
                        info = info,
                        index = index,
                        selectIndex = selectIndex.value
                    ) {
                        selectIndex.value = it
                        changedCardNumber = cardList[it].cardNumber
                    }
                    if (index < cardList.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .height(1.dp)
                                .background(Gray)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        FooterWithButton(text = "선택하기") {
            selectChangerListener(changedCardNumber)
        }

    }
}

@Composable
fun MethodOfPaymentCardItem(
    info: CardInfo,
    index: Int,
    selectIndex: Int,
    selectChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
            .nonRippleClickable { selectChange(index) }
    ) {
        AsyncImage(
            model = info.image,
            contentDescription = null,
            modifier = Modifier.size(58.dp, 36.dp)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = info.name, style = getTextStyle(12, false, DarkGray))
            Text(
                text = info.balance.toPriceFormat(),
                style = getTextStyle(16, true, Black),
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CustomCheckBox(
            text = "",
            selected = index == selectIndex,
            checkedIcon = R.drawable.ic_checkbox_check,
            uncheckedIcon = R.drawable.ic_checkbox_unchecked,
            onClick = {
                selectChange(index)
            }
        )
    }
}