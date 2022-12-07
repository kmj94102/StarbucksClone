package com.example.starbucksclone.view.main.pay.registration

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.*
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CardRegistrationScreen(
    routeAction: RouteAction,
    viewModel: CardRegistrationViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    val pagerState = rememberPagerState()
    val keyboardState = keyboardAsState()
    val tabItems = listOf("스타벅스 카드", "카드 교환권")
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            /** 해더 영역 **/
            stickyHeader {
                CardRegistrationHeader(
                    routeAction = routeAction,
                    isScrolled = state.isScrolled,
                    pagerState = pagerState,
                    tabItems = tabItems
                )
            }
            /** 바디 영역 **/
            item {
                CardRegistrationBody(
                    pagerState = pagerState,
                    routeAction = routeAction,
                    tabItems = tabItems,
                    viewModel = viewModel
                )
            }
        }
        /** 풋터 영역 **/
        if (pagerState.currentPage == 0 && keyboardState.value == Keyboard.Closed) {
            CardRegistrationFooter(viewModel)
        }
    }

    when(val status = viewModel.status.collectAsState().value) {
        is CardRegistrationViewModel.CardRegistrationStatus.Init -> {}
        is CardRegistrationViewModel.CardRegistrationStatus.Failure -> {
            context.toast(status.msg)
        }
        is CardRegistrationViewModel.CardRegistrationStatus.Success -> {
            context.toast("카드 등록을 완료하였습니다.")
            routeAction.popupBackStack()
        }
    }

}

/** 해더 영역 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRegistrationHeader(
    routeAction: RouteAction,
    isScrolled: Boolean,
    pagerState: PagerState,
    tabItems: List<String>
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxWidth()) {
        MainTitle(
            titleText = "카드 추가",
            onLeftIconClick = {
                routeAction.popupBackStack()
            },
            isExpand = isScrolled.not()
        )

        CustomTabRow(
            pagerState = pagerState,
            tabItems = tabItems,
            coroutineScope = scope,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** 바디 영역 **/
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRegistrationBody(
    pagerState: PagerState,
    routeAction: RouteAction,
    tabItems: List<String>,
    viewModel: CardRegistrationViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            count = tabItems.size,
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            when (it) {
                0 -> {
                    StarbucksCard(
                        routeAction = routeAction,
                        viewModel = viewModel,
                    )
                }
                1 -> {
                    CardCoupon(routeAction)
                }
            }
        }
    }
}

/** 스타벅스 카드 **/
@Composable
fun StarbucksCard(
    routeAction: RouteAction,
    viewModel: CardRegistrationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp, vertical = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CommonTextField(
                value = viewModel.cardInfo.value.cardName,
                onValueChange = {
                    if (it.length <= 12) {
                        viewModel.event(
                            CardRegistrationEvent.TextChange(
                                text = it,
                                type = CardRegistrationViewModel.CardName
                            )
                        )
                    }
                },
                hint = "카드명 최대 20자 (선택)",
                imeAction = ImeAction.Next
            )
            Text(
                text = "카드명은 미입력 시 자동으로 부여됩니다.",
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        CommonTextField(
            value = viewModel.cardInfo.value.cardNumber,
            onValueChange = {
                if (it.length <= 16) {
                    viewModel.event(
                        CardRegistrationEvent.TextChange(
                            text = it,
                            type = CardRegistrationViewModel.CardNumber
                        )
                    )
                }
            },
            hint = "스타벅스 카드번호 16자리 (필수)",
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(10.dp))

        CommonTextField(
            value = viewModel.cardInfo.value.pinNumber,
            onValueChange = {
                if (it.length <= 8) {
                    viewModel.event(
                        CardRegistrationEvent.TextChange(
                            text = it,
                            type = CardRegistrationViewModel.PinNumber
                        )
                    )
                }
            },
            hint = "Pin번호 8자리 (필수)",
            keyboardType = KeyboardType.Number
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGray)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("스타벅스 카드 등록 시, 실물 카드와 카드 바코드 모두 사용 가능합니다.\n\n카드가 없다면 e-Gift Card의 ")
                    withStyle(SpanStyle(color = MainColor, fontWeight = FontWeight.Bold)) {
                        append("나에게 선물하기")
                    }
                    append("를 이용해보세요. 카드명은 미입력 시 자동으로 부여됩니다.")
                },
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }

}

/** 카드 교환권 **/
@Composable
fun CardCoupon(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(vertical = 18.dp, horizontal = 23.dp)
                .background(LightGray)
        ) {
            Text(
                text = "모바일 카드 교환권을 등록하여 스타벅스 카드로 사용하세요",
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(vertical = 9.dp, horizontal = 22.dp)
            )
        }
        CardCouponItem(
            imageRes = R.drawable.ic_image,
            text = "교환권 이미지 불러오기",
            onClickListener = {}
        )
        CardCouponItem(
            imageRes = R.drawable.ic_barcode,
            text = "바코드 인식하기",
            onClickListener = {}
        )
        CardCouponItem(
            imageRes = R.drawable.ic_password,
            text = "교환권 번호 입력하기",
            onClickListener = {}
        )
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(LightGray)
        ) {
            Text(
                text = "모바일 카드 교환권이란?",
                style = getTextStyle(14, true),
                modifier = Modifier.padding(top = 27.dp, start = 23.dp, end = 23.dp)
            )
            Text(
                text = "카카오톡 선물하기 등 타사에서 발행하는 모바일 상품권으로, 스타벅스 카드로 교환하여 사용하실 수 있습니다.\n\n" +
                        "교환권을 등록하면 상품권에 표시된 금액이 충전된 스타벅스 카드가 신규 발급된다.\n\n" +
                        "카드 교환권 등록 취소는 카드 미사용 시에 한하여 7일 이내에 가능합니다.",
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 150.dp,
                    start = 23.dp,
                    end = 23.dp
                )
            )
        }
    }
}

@Composable
fun CardCouponItem(
    @DrawableRes imageRes: Int,
    text: String,
    onClickListener: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .nonRippleClickable { onClickListener() }
    ) {
        Spacer(modifier = Modifier.width(26.dp))
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.padding(vertical = 28.dp)
        )
        Text(
            text = text, style = getTextStyle(14), modifier = Modifier
                .weight(1f)
                .padding(14.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = DarkGray
        )
        Spacer(modifier = Modifier.width(15.dp))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray)
    )
}

/** 풋터 영역 **/
@Composable
fun CardRegistrationFooter(viewModel: CardRegistrationViewModel) {
    Surface(
        elevation = 6.dp,
        color = White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomCheckBox(
                text = "스타벅스 카드 이용약관 [필수]",
                selected = viewModel.isTermsCheck.value,
                checkedIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_checkbox_check),
                        contentDescription = "checkbox checked",
                    )
                },
                uncheckedIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_checkbox_unchecked),
                        contentDescription = "checkbox unchecked",
                        tint = MainColor
                    )
                },
                isNextButton = true,
                onClick = { viewModel.event(CardRegistrationEvent.TermsCheck) },
                modifier = Modifier.padding(horizontal = 23.dp, vertical = 10.dp)
            )

            RoundedButton(
                text = "등록하기",
                isEnabled = viewModel.isEnable.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 23.dp, end = 23.dp)
            ) {
                viewModel.event(CardRegistrationEvent.CardRegistration)
            }
        }
    }
}