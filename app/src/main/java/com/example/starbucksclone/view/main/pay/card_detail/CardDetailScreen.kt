package com.example.starbucksclone.view.main.pay.card_detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun CardDetailScreen(
    routAction: RoutAction,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val isDialogShow = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CardDetailHeader(
            cardInfo = viewModel.cardInfo.value,
            routAction = routAction,
            isDialogShow = isDialogShow
        )
        CardDetailBody(modifier = Modifier.weight(1f))
    }

    CommonTitleDialog(
        title = "카드 이름을 입력해주세요.",
        isShow = isDialogShow.value,
        okText = "확인",
        cancelText = "취소",
        okClickListener = {
            isDialogShow.value = false
            viewModel.event(CardDetailEvent.UpdateCardName)
        },
        cancelClickListener = {
            isDialogShow.value = false
        }
    ) {
        val value = remember { mutableStateOf(viewModel.cardInfo.value.name) }
        CommonTextField(
            value = value.value,
            onValueChange = {
                value.value = it
                viewModel.event(CardDetailEvent.ModifyCardName(it))
            },
            hint = "카드 이름",
            isLabel = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
    }

    when(viewModel.status.collectAsState().value) {
        is CardDetailViewModel.Status.Init -> {}
        is CardDetailViewModel.Status.Success -> {
            context.toast("카드 이름 변경이 완료되었습니다.")
        }
        is CardDetailViewModel.Status.Failure -> {
            context.toast("카드 이름 변경을 실패하였습니다.")
        }
    }

}

@Composable
fun CardDetailHeader(
    cardInfo: CardInfo,
    routAction: RoutAction,
    isDialogShow: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "back",
            modifier = Modifier
                .padding(top = 10.dp, start = 14.dp)
                .nonRippleClickable {
                    routAction.popupBackStack()
                }
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, start = 23.dp, end = 11.dp)
        ) {
            Text(
                text = cardInfo.name,
                style = Typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_modify),
                contentDescription = "",
                modifier = Modifier.nonRippleClickable {
                    isDialogShow.value = true
                }
            )
        }

        Spacer(modifier = Modifier.height(43.dp))
        Row {
            Spacer(modifier = Modifier.width(23.dp))
            AsyncImage(
                model = cardInfo.image,
                contentDescription = "",
                modifier = Modifier.size(115.dp, 73.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 13.dp)
            ) {
                Text(
                    text = "카드 잔액",
                    style = Typography.body1,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = cardInfo.balance.toPriceFormat(),
                    style = Typography.subtitle2,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(21.dp))
    }
}

@Composable
fun CardDetailBody(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        CardDetailSelector(
            imgRes = R.drawable.ic_usding_history,
            text = "이용 내역"
        )
        CardDetailSelector(
            imgRes = R.drawable.ic_usding_history,
            text = "자동 충전",
            isAutoCharging = false
        )
        CardDetailSelector(
            imgRes = R.drawable.ic_usding_history,
            text = "일반 충전"
        )
        CardDetailSelector(
            imgRes = R.drawable.ic_usding_history,
            text = "분실 신고 및 잔액 이전"
        )
        CardDetailSelector(
            imgRes = R.drawable.ic_usding_history,
            text = "카드 등록 해지"
        )
    }
}

@Composable
fun CardDetailSelector(
    @DrawableRes imgRes: Int,
    text: String,
    isAutoCharging: Boolean? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 17.dp, bottom = 17.dp, start = 27.dp, end = 15.dp)
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = Typography.body1,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(10.dp))
        isAutoCharging?.let {
            Text(
                text = if (isAutoCharging) "ON" else "OFF",
                style = Typography.body1,
                fontSize = 14.sp,
                color = DarkGray
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = "")
    }
}