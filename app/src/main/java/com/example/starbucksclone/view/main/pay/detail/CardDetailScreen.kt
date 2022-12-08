package com.example.starbucksclone.view.main.pay.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CardItem
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun CardDetailScreen(
    routeAction: RouteAction,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        /** 해더 영역 **/
        CardDetailHeader(
            title = viewModel.cardInfo.value.name,
            routeAction = routeAction,
            viewModel = viewModel
        )
        /** 바디 영역 **/
        CardDetailBody(
            cardInfo = viewModel.cardInfo.value,
            routeAction = routeAction,
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
    }

    val context = LocalContext.current
    when(val status = viewModel.status.collectAsState().value) {
        is CardDetailViewModel.CardDetailStatus.Init -> {}
        is CardDetailViewModel.CardDetailStatus.CardDeleteSuccess -> {
            context.toast("카드 삭제를 완료하였습니다.")
            routeAction.popupBackStack()
        }
        is CardDetailViewModel.CardDetailStatus.Error -> {
            context.toast(status.msg)
        }
    }
}

/** 해더 영역 **/
@Composable
fun CardDetailHeader(
    title: String,
    routeAction: RouteAction,
    viewModel: CardDetailViewModel
) {
    val isCardNameModifyVisible = remember {
        mutableStateOf(false)
    }

    MainTitle(
        titleText = title,
        onLeftIconClick = {
            routeAction.popupBackStack()
        },
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_modify),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 12.dp)
                    .nonRippleClickable {
                        isCardNameModifyVisible.value = true
                    }
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    CommonTitleDialog(
        isShow = isCardNameModifyVisible.value,
        title = "카드 이름을 입력해주세요.",
        contents = {
            CommonTextField(
                value = viewModel.modifyCardName.value,
                onValueChange = {
                    viewModel.event(CardDetailEvent.CardNameChange(it))
                },
                hint = "카드 이름",
                isLabel = true,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        },
        okText = "확인",
        okClickListener = {
            isCardNameModifyVisible.value = false
            viewModel.event(CardDetailEvent.CardNameModify)
        },
        cancelText = "취소",
        cancelClickListener = {
            isCardNameModifyVisible.value = false
            viewModel.event(CardDetailEvent.InitCardNameModify)
        }
    )
}

/** 바디 영역 **/
@Composable
fun CardDetailBody(
    cardInfo: CardInfo,
    routeAction: RouteAction,
    viewModel: CardDetailViewModel,
    modifier: Modifier = Modifier
) {
    val isCancelRegistrationShow = remember {
        mutableStateOf(false)
    }

    CardItem(
        cardInfo = cardInfo,
        title = "카드 잔액",
        isBigSize = true,
        modifier = Modifier.padding(top = 30.dp, start = 23.dp)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(LightGray)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        CardDetailBodyItem(
            iconRes = R.drawable.ic_usage_history,
            text = "이용 내역",
        ) {}
        CardDetailBodyItem(
            iconRes = R.drawable.ic_auto_charging,
            text = "자동 충전",
            isAutoCharging = true
        ) {
            routeAction.goToScreenWithCardNumber(
                page = RouteAction.CardCharging,
                cardNumber = cardInfo.cardNumber
            )
        }
        CardDetailBodyItem(
            iconRes = R.drawable.ic_charging,
            text = "일반 충전",
        ) {
            routeAction.goToScreenWithCardNumber(
                page = RouteAction.CardCharging,
                cardNumber = cardInfo.cardNumber
            )
        }
        CardDetailBodyItem(
            iconRes = R.drawable.ic_report_loss,
            text = "분실 신고 및 잔액 이전",
        ) {}
        CardDetailBodyItem(
            iconRes = R.drawable.ic_minus_circle,
            text = "카드 등록 해지",
        ) {
            isCancelRegistrationShow.value = true
        }
    }

    CommonTitleDialog(
        isShow = isCancelRegistrationShow.value,
        title = if (cardInfo.balance > 0) {
            "잔액이 남아있는 카드입니다. 카드 등록을 해지하시겠어요?"
        } else {
            "카드 등록을 해지하시겠어요?"
        },
        contents = {
            if (cardInfo.balance > 0) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MainColor)) {
                            append("카드 잔액 : ${cardInfo.balance.toPriceFormat()}\n")
                        }
                        append(
                            "- 본인 인증 완료 후 카드 등록해지가 가능합니다.\n\n" +
                                    "- '예'버튼 선택 시 등록된 휴대폰 번호로 인증번호가 발송됩니다.(인증생략)\n\n" +
                                    "- 등록을 해지하시면 Pay 리스트에서 삭제됩니다. 실물카드가 없는 경우, 재등록이 불가합니다."
                        )
                    },
                    style = getTextStyle(14),
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 28.dp)
                )
            }
        },
        okClickListener = {
            isCancelRegistrationShow.value = false
            viewModel.event(CardDetailEvent.CardDelete)
        },
        cancelClickListener = {
            isCancelRegistrationShow.value = false
        }
    )

}

/** 바디 아이템 **/
@Composable
fun CardDetailBodyItem(
    @DrawableRes iconRes: Int,
    text: String,
    isAutoCharging: Boolean = false,
    onClickListener: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 17.dp, bottom = 17.dp, start = 27.dp, end = 15.dp)
            .nonRippleClickable {
                onClickListener()
            }
    ) {
        Image(painter = painterResource(id = iconRes), contentDescription = null)
        Text(
            text = text,
            style = getTextStyle(14),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
        )
        if (isAutoCharging) {
            Text(
                text = "OFF",
                style = getTextStyle(14, true, DarkGray),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = null)
    }
}