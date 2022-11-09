package com.example.starbucksclone.view.main.pay.registration

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardRegistrationInfo
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.*
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun CardRegistrationScreen(
    routAction: RoutAction,
    viewModel: CardRegistrationViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        /** 해더 영역 **/
        CardRegistrationHeader(routAction)
        /** 바디 영역 **/
        CardRegistrationBody(viewModel)
    }

    val status = viewModel.status.collectAsState()
    when(status.value) {
        is CardRegistrationViewModel.Status.Init -> {}
        is CardRegistrationViewModel.Status.Loading -> {}
        is CardRegistrationViewModel.Status.Success -> {
            context.toast("카드 등록이 완료되었습니다.")
            routAction.popupBackStack()
        }
        is CardRegistrationViewModel.Status.Failure -> {
            context.toast("카드 등록에 실패하였습니다.")
        }
    }

}

/** 해더 영역 **/
@Composable
fun CardRegistrationHeader(routAction: RoutAction) {
    Column {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "titleIcon",
            modifier = Modifier
                .nonRippleClickable { routAction.popupBackStack() }
                .padding(start = 8.dp, top = 8.dp)
        )
        Text(
            text = "카드 추가",
            style = Typography.subtitle1,
            fontSize = 26.sp,
            modifier = Modifier.padding(top = 18.dp, start = 15.dp)
        )
    }
}

/** 바디 영역 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRegistrationBody(
    viewModel: CardRegistrationViewModel,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()
    val tabItems = listOf("스타벅스 카드", "카드 교환권")
    val scope = rememberCoroutineScope()
    /** 텝 영역 **/
    CustomTabRow(
        pagerState = pagerState,
        tabItems = tabItems,
        coroutineScope = scope
    )
    HorizontalPager(
        count = 2,
        state = pagerState,
        modifier = modifier,
    ) {
        when (it) {
            0 -> {
                StarbucksCardPage(viewModel)
            }
            1 -> {
                CardCouponPage()
            }
        }
    }
}

@Composable
fun StarbucksCardPage(
    viewModel: CardRegistrationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val info = viewModel.cardRegistrationInfo.value
        Spacer(modifier = Modifier.height(10.dp))
        CommonTextField(
            value = info.cardName,
            onValueChange = {
                viewModel.event(
                    CardRegistrationEvent.InputInfo(
                        type = CardRegistrationViewModel.CardName,
                        info = it
                    )
                )
            },
            imeAction = ImeAction.Next,
            hint = "카드명 최대 20자 (선택)",
            focusedIndicatorColor = Black,
            isLabel = true,
            supportText = "카드명은 미입력 시 자동으로 부여됩니다.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        CommonTextField(
            value = info.cardNumber,
            onValueChange = {
                viewModel.event(
                    CardRegistrationEvent.InputInfo(
                        type = CardRegistrationViewModel.CardNumber,
                        info = it
                    )
                )
            },
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            hint = "스타벅스 카드번호 16자리 (필수)",
            trailingIcons = listOf(R.drawable.ic_care),
            isError = viewModel.isCardNumberError.value,
            errorText = if (info.cardNumber.isEmpty()) "스타벅스 카드 번호를 입력해주세요." else "올바른 스타벅스 카드 번호를 입력해 주세요.",
            focusedIndicatorColor = Black,
            isLabel = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        CommonTextField(
            value = info.pinNumber,
            onValueChange = {
                viewModel.event(
                    CardRegistrationEvent.InputInfo(
                        type = CardRegistrationViewModel.PinNumber,
                        info = it
                    )
                )
            },
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done,
            hint = "Pin번호 8자리 (필수)",
            visualTransformation = PasswordVisualTransformation(),
            trailingIcons = listOf(R.drawable.ic_care, R.drawable.ic_information_brown),
            isError = viewModel.isPinNumberError.value,
            errorText = if (info.pinNumber.isEmpty()) "Pin번호를 입력해주세요." else "올바른 Pin번호를 입력해 주세요.",
            focusedIndicatorColor = Black,
            isLabel = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        )

        Spacer(modifier = Modifier.height(0.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
                .background(LightGray)
        ) {
            Text(
                buildAnnotatedString {
                    append("스타벅스 카드 등록 시, 실물카드와 카드 바코드 모두 사용 가능합니다.\n카드가 없다면 e-Gift Card의 ")
                    withStyle(style = SpanStyle(color = MainColor)) {
                        append("나에게 선물하기")
                    }
                    append("를 이용해보세요. 카드명은 미입력 시 자동으로 부여됩니다.")
                },
                fontSize = 14.sp,
                style = Typography.caption,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Surface(
            shadowElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.background(White)) {
                CommonCheckBox(
                    text = "스타벅스 카드 이용약관 동의 [필수]",
                    selected = viewModel.isAgreeTerms.value,
                    onClick = { viewModel.event(CardRegistrationEvent.AgreeTermsCheck) },
                    isNextButton = true,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp)
                )
                RoundedButton(
                    text = "등록하기",
                    isEnabled = viewModel.isEnable.value,
                    textColor = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 23.dp, end = 23.dp, bottom = 10.dp)
                ) {
                    viewModel.event(CardRegistrationEvent.CardRegistration)
                }
            }
        }
    }
}

@Composable
fun CardCouponPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(LightGray)
        ) {
            Text(
                text = "모바일카드 교환권을 등록하여 스타벅스 카드로 사용하세요.",
                style = Typography.caption,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp)
            )
        }

        CardCouponPageItem(
            imgRes = R.drawable.ic_image,
            text = "교환권 이미지 불러오기",
            modifier = Modifier.padding(top = 54.dp)
        )
        CardCouponPageItem(
            imgRes = R.drawable.ic_barcode,
            text = "바코드 인식하기",
            modifier = Modifier.padding(top = 27.dp)
        )
        CardCouponPageItem(
            imgRes = R.drawable.ic_password,
            text = "교환권 번호 입력하기",
            modifier = Modifier.padding(top = 27.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGray)
        ) {
            Text(
                text = "모바일 카드 교환권이란?",
                style = Typography.body2,
                modifier = Modifier.padding(top = 27.dp, start = 23.dp)
            )
            Text(
                text = "카카오톡 선물하기 등 타사에서 발행하는 모바일 상품권으로, 스타벅스 카드로 교환하여 사용하실 수 있습니다.\n\n교환권을 등록하면 상품권에 표시된 금액이 충전된 스타벅스 카드가 신규 발급됩니다.\n\n카드 교환권 등록 취소는 카드 미사용 시에 한하여 7일 이내에 가능합니다.",
                style = Typography.caption,
                modifier = Modifier.padding(top = 19.dp, start = 23.dp, end = 23.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun CardCouponPageItem(
    @DrawableRes imgRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .nonRippleClickable {
                context.toast("준비중입니다.")
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = null,
                modifier = Modifier.padding(start = 26.dp)
            )
            Text(
                text = text,
                style = Typography.body2,
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = BorderColor,
                modifier = Modifier
                    .padding(end = 15.dp)
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
                .height(1.dp)
                .background(LightGray)
        )
    }
}