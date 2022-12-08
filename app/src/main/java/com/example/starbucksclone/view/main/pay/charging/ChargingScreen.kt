package com.example.starbucksclone.view.main.pay.charging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.*
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RouteAction
import okhttp3.internal.toLongOrDefault

@Composable
fun ChargingScreen(
    routeAction: RouteAction,
    viewModel: ChargingViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        /** 해더 영역 **/
        ChargingHeader(
            routeAction = routeAction,
            isExpand = state.isScrolled.not()
        )

        /** 바디 영역 **/
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                ChargingBody(viewModel = viewModel)
            }
        }

        /** 풋터 영역 **/
        ChargingFooter(viewModel)
    }

    val context = LocalContext.current
    when (viewModel.status.collectAsState().value) {
        is ChargingViewModel.ChargingStatus.Init -> {}
        is ChargingViewModel.ChargingStatus.EmptyCardNumber -> {
            context.toast("오류가 발생하였습니다. 잠시 후 다시 시도해주세요.")
            routeAction.popupBackStack()
        }
        is ChargingViewModel.ChargingStatus.Failure -> {
            context.toast("오류가 발생하였습니다. 잠시 후 다시 시도해주세요.")
        }
        is ChargingViewModel.ChargingStatus.Success -> {
            context.toast("충전을 완료하였습니다.")
            routeAction.popupBackStack()
        }
    }
}

/** 해더 영역 **/
@Composable
fun ChargingHeader(
    routeAction: RouteAction,
    isExpand: Boolean
) {
    MainTitle(
        titleText = "일반 충전",
        isExpand = isExpand,
        onLeftIconClick = {
            routeAction.popupBackStack()
        },
        modifier = Modifier.fillMaxWidth()
    )
}

/** 바디 영역 **/
@Composable
fun ChargingBody(
    viewModel: ChargingViewModel
) {
    val amount = viewModel.chargingAmount.value
    val isShow = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 23.dp)
    ) {
        CardItem(
            cardInfo = viewModel.cardInfo.value,
            modifier = Modifier.padding(top = 30.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp, bottom = 30.dp)
                .height(1.dp)
                .background(LightGray)
        )

        Text(text = "충전 금액", style = getTextStyle(16, true))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            SelectButton(
                isSelected = amount == 10_000L,
                text = "1만원"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(10_000))
            }
            SelectButton(
                isSelected = amount == 30_000L,
                text = "3만원"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(30_000))
            }
            SelectButton(
                isSelected = amount == 50_000L,
                text = "5만원"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(50_000))
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            SelectButton(
                isSelected = amount == 70_000L,
                text = "7만원"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(70_000))
            }
            SelectButton(
                isSelected = amount == 100_000L,
                text = "10만원"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(100_000))
            }
            SelectButton(
                isSelected = amount != 10_000L && amount != 30_000L && amount != 50_000L &&
                        amount != 70_000L && amount != 100_000L && amount != 0L,
                text = "다른 금액"
            ) {
                viewModel.event(ChargingEvent.ChargingAmountChange(0))
                isShow.value = true
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
                .height(1.dp)
                .background(LightGray)
        )

        Text(text = "온라인 충전 시 유의사항", style = getTextStyle(16, true))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 23.dp, top = 30.dp)
                .background(LightGray)
        ) {
            Text(
                text = "스타벅스 카드 충전은 1회 1만원부터 55만원까지 가능하며, 충전 후 총액이 55만원을 초과할 수 없습니다.",
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp)
            )
        }

        DefaultAmountDialog(
            isShow = isShow.value,
            currentAmount = viewModel.cardInfo.value.balance,
            okClickListener = {
                isShow.value = false
                viewModel.event(ChargingEvent.ChargingAmountChange(it))
            },
            cancelClickListener = {
                isShow.value = false
            }
        )
    }
}

@Composable
fun DefaultAmountDialog(
    isShow: Boolean,
    currentAmount: Long,
    okClickListener: (Long) -> Unit,
    cancelClickListener: () -> Unit
) {
    val amount = remember {
        mutableStateOf("")
    }
    val expectedBalance = currentAmount + (amount.value.toLongOrDefault(0L) * 10_000)

    CommonTitleDialog(
        title = "1만원 부터 55원 까지 충전 가능합니다.",
        isShow = isShow,
        okText = "확인",
        okClickListener = {
            okClickListener(amount.value.toLong() * 10_000)
        },
        okButtonEnable = amount.value.toLongOrDefault(0L) != 0L &&
                expectedBalance <= 550_000,
        cancelText = "취소",
        cancelClickListener = cancelClickListener,
        contents = {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, bottom = 28.dp)
            ) {
                val (textField, errorText, won) = createRefs()
                CommonTextField(
                    value = amount.value,
                    onValueChange = {
                        if (specialCharacterRestrictions(it)) {
                            amount.value = it
                        }
                    },
                    hint = "충전 금액 (1만원 단위)",
                    isError = expectedBalance > 550_000,
                    modifier = Modifier.constrainAs(textField) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(won.start)
                        width = Dimension.fillToConstraints
                    }
                )
                if (expectedBalance > 550_000) {
                    Text(
                        text = "스타벅스 카드 충전은 1회 1만원부터 55만원까지 가능하며, 충전 후 총액이 55만원을 초과할 수 없습니다.",
                        style = getTextStyle(size = 12, color = HotColor),
                        modifier = Modifier.constrainAs(errorText) {
                            top.linkTo(textField.bottom, (-15).dp)
                            start.linkTo(textField.start)
                        }
                    )
                }

                Text(
                    text = "만원",
                    style = getTextStyle(12),
                    modifier = Modifier.constrainAs(won) {
                        start.linkTo(textField.end, 5.dp)
                        end.linkTo(parent.end)
                        baseline.linkTo(textField.baseline, 15.dp)
                    }
                )
            }
        }
    )
}

/** 풋터 영역 **/
@Composable
fun ChargingFooter(
    viewModel: ChargingViewModel
) {
    Surface(
        shadowElevation = 6.dp,
        color = White
    ) {
        val expectedBalance = viewModel.cardInfo.value.balance + viewModel.chargingAmount.value
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            ) {
                Text(text = "충전 후 예상 총 카드 잔액", style = getTextStyle(12, true, Color(0xFF906E4B)))
                Text(
                    text = expectedBalance.toPriceFormat(),
                    style = getTextStyle(
                        size = 20,
                        isBold = true,
                        color = if (expectedBalance <= 550_000) Black else HotColor
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            RoundedButton(
                text = "충전하기",
                isEnabled = expectedBalance != viewModel.cardInfo.value.balance &&
                        expectedBalance <= 550_000,
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.event(ChargingEvent.CardCharging)
            }
        }
    }
}