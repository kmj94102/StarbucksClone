package com.example.starbucksclone.view.main.order.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentScreen(
    routeAction: RouteAction
) {
    val state = rememberLazyListState()
    val modalState = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        isSkipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = {
//            MethodOfPaymentBottomSheet()
            OrderResultBottomSheet()
        },
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            /** 해더 영역 **/
            PaymentHeader(routeAction)
            /** 바디 영억 **/
            PaymentBody(
                state = state,
                modifier = Modifier.weight(1f)
            ) {
                scope.launch {
                    modalState.show()
                }
            }
            /** 풋터 영역 **/
            PaymentFooter()
        }
    }
}

/** 해더 영역 **/
@Composable
fun PaymentHeader(
    routeAction: RouteAction
) {
    MainTitle(titleText = "결제하기", onLeftIconClick = { routeAction.popupBackStack() })
}

/** 바디 영역 **/
@Composable
fun PaymentBody(
    state: LazyListState,
    modifier: Modifier = Modifier,
    onClickListener: () -> Unit
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(top = 33.dp, bottom = 100.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            /** 결제 수단 **/
            MethodOfPayment {
                onClickListener()
            }
            /** 쿠폰 및 할인 **/
            CouponsAndDiscounts()
            /** 현금 영수증 **/
            CashReceipts()
            /** 주문 내역 **/
            OrderHistory()
            /** 최종 결제 금액 **/
            FinalPayment()
        }
    }
}

/** 결제 수단 **/
@Composable
fun MethodOfPayment(
    onClickListener: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .nonRippleClickable {
                onClickListener()
            }
    ) {
        Text(
            text = "결제 수단",
            style = getTextStyle(16, true),
            modifier = Modifier.padding(horizontal = 23.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 23.dp)
        ) {
            AsyncImage(
                model = "https://image.istarbucks.co.kr/cardImg/20220907/009446_WEB.png",
                contentDescription = null,
                modifier = Modifier
                    .size(58.dp, 37.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(text = "스타벅스 카드", style = getTextStyle(12, false, DarkGray))
                Text(
                    text = 70.priceFormat(),
                    style = getTextStyle(16, true, Black),
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            Text(text = "잔액 부족", style = getTextStyle(12, false, HotColor))
            Image(
                painter = painterResource(id = R.drawable.ic_care),
                contentDescription = null,
                modifier = Modifier.padding(start = 2.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                modifier = Modifier.padding(start = 13.dp, end = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, top = 23.dp)
                .height(1.dp)
                .background(Gray)
        )
    }

}

/** 쿠폰 및 할인 **/
@Composable
fun CouponsAndDiscounts() {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val rotate = animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 27.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "쿠폰 및 할인",
                style = getTextStyle(16, true),
                modifier = Modifier
                    .padding(start = 23.dp)
                    .weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .rotate(rotate.value)
                    .nonRippleClickable {
                        isExpanded = isExpanded.not()
                    }
            )
        }

        AnimatedVisibility(
            visible = isExpanded
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 23.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coupon),
                        contentDescription = "coupon",
                        modifier = Modifier.padding(vertical = 17.dp)
                    )
                    Text(
                        text = "쿠폰",
                        style = getTextStyle(14),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_gift_code),
                        contentDescription = "gift",
                        modifier = Modifier.padding(vertical = 17.dp)
                    )
                    Text(
                        text = "선물",
                        style = getTextStyle(14),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_carrier_discount),
                        contentDescription = "carrier discount",
                        modifier = Modifier.padding(top = 17.dp)
                    )
                    Text(
                        text = "통신사 제휴 할인",
                        style = getTextStyle(14),
                        modifier = Modifier.padding(start = 12.dp, top = 17.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(27.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp)
                .height(1.dp)
                .background(Gray)
        )
    }
}

/** 현금 영수증 **/
@Composable
fun CashReceipts() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 27.dp)
    ) {
        Text(
            text = "현금영수증",
            style = getTextStyle(16, true),
            modifier = Modifier.padding(horizontal = 23.dp)
        )
    }
}

/** 주문 내역 **/
@Composable
fun OrderHistory() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)
    ) {
        Spacer(modifier = Modifier.height(27.dp))
        Text(
            text = "주문 내역",
            style = getTextStyle(16, true),
            modifier = Modifier.padding(horizontal = 23.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        (0..3).forEachIndexed { index, _ ->
            HistoryItem()
            if (index < 3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 23.dp)
                        .height(1.dp)
                        .background(Gray)
                )
            }
        }
    }
}

/** 주문 내역 아이템 **/
@Composable
fun HistoryItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp, vertical = 27.dp)
    ) {
        CircleImage(
            imageURL = "https://image.istarbucks.co.kr/cardImg/20220907/009446_WEB.png",
            modifier = Modifier.size(46.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 11.dp)
                .weight(1f)
        ) {
            Text(text = "디카페인 카페 아메리카노", style = getTextStyle(12, false, Black))
            Text(
                text = "HOT | Tall | 매장컵",
                style = getTextStyle(12, false, DarkGray),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Column {
            Text(text = 4400.priceFormat(), style = getTextStyle(12, true, Black))
            Text(
                text = 4400.priceFormat(),
                style = getTextStyle(12, false, DarkGray),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/** 최종 결제 금액 **/
@Composable
fun FinalPayment() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 27.dp)
    ) {
        Text(
            text = "최종 결제 금액",
            style = getTextStyle(16, true),
            modifier = Modifier
                .padding(start = 23.dp)
                .weight(1f)
        )
        Text(text = 4400.priceFormat(), style = getTextStyle(20, true))
    }
}

@Composable
fun PaymentFooter() {
    FooterWithButton(text = "4,400원 결제하기") {

    }
}