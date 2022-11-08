package com.example.starbucksclone.view.main.pay.registration

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.view.common.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun CardRegistrationScreen() {
    val lazyListState = rememberLazyListState()
    val pagerState = rememberPagerState()


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            stickyHeader {
                CardRegistrationHeader(
                    lazyListState = lazyListState,
                    pagerState = pagerState
                )
            }
            item {
                CardRegistrationBody(
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        FooterWithButton(text = "등록하기") {

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRegistrationHeader(
    lazyListState: LazyListState,
    pagerState: PagerState
) {
    val tabItems = listOf("스타벅스 카드", "카드 교환권")
    val scope = rememberCoroutineScope()

    /** 타이틀 영역 **/
    MotionTitle(
        titleText = "카드 추가",
        lazyListSate = lazyListState
    )
    /** 텝 영역 **/
    CustomTabRow(
        pagerState = pagerState,
        tabItems = tabItems,
        coroutineScope = scope
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardRegistrationBody(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    viewModel: CardRegistrationViewModel = hiltViewModel()
) {
    HorizontalPager(
        count = 2,
        state = pagerState,
        modifier = modifier,
    ) {
        when (pagerState.currentPage) {
            0 -> {
                StarbucksCardPage(viewModel)
            }
            else -> {
                CardCouponPage()
            }
        }
    }
}

@Composable
fun StarbucksCardPage(viewModel: CardRegistrationViewModel) {
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
    }
}

@Composable
fun CardCouponPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.Cyan)
    ) {

    }
}