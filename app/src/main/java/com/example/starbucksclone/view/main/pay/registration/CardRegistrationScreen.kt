package com.example.starbucksclone.view.main.pay.registration

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CardRegistrationScreen(
    routeAction: RouteAction,
    viewModel: CardRegistrationViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    val pagerState = rememberPagerState()
    val keyboardState = keyboardAsState()
    val tabItems = listOf("???????????? ??????", "?????? ?????????")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val modalState = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        isSkipHalfExpanded = true
    )

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = {
            when(viewModel.modalState.value) {
                1 -> {
                    BarcodeBottomSheetContents(
                        decodedListener = {
                            viewModel.event(CardRegistrationEvent.BarcodeRegistration(it))
                            scope.launch {
                                modalState.hide()
                            }
                        },
                        errorListener = {
                            scope.launch {
                                modalState.hide()
                                context.toast("????????? ????????? ?????????????????????.")
                            }
                        }
                    )
                }
                2 -> {
                    VoucherBottomSheetContents{
                        viewModel.event(CardRegistrationEvent.CouponRegistration)
                        scope.launch {
                            modalState.hide()
                        }
                    }
                }
                else -> { Box(modifier = Modifier.fillMaxWidth().height(1.dp)) }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                /** ?????? ?????? **/
                stickyHeader {
                    CardRegistrationHeader(
                        routeAction = routeAction,
                        isScrolled = state.isScrolled,
                        pagerState = pagerState,
                        tabItems = tabItems
                    )
                }
                /** ?????? ?????? **/
                item {
                    CardRegistrationBody(
                        pagerState = pagerState,
                        routeAction = routeAction,
                        tabItems = tabItems,
                        viewModel = viewModel,
                        modalState = modalState
                    )
                }
            }
            /** ?????? ?????? **/
            if (pagerState.currentPage == 0 && keyboardState.value == Keyboard.Closed) {
                CardRegistrationFooter(viewModel)
            }
        }
    }

    BackPressHandler {
        if (modalState.isVisible) {
            scope.launch {
                modalState.hide()
            }
        } else {
            routeAction.popupBackStack()
        }
    }

    when (val status = viewModel.status.collectAsState().value) {
        is CardRegistrationViewModel.CardRegistrationStatus.Init -> {}
        is CardRegistrationViewModel.CardRegistrationStatus.Failure -> {
            context.toast(status.msg)
        }
        is CardRegistrationViewModel.CardRegistrationStatus.Success -> {
            if (routeAction.getCurrentRoute(RouteAction.CardRegistration)) {
                context.toast("?????? ????????? ?????????????????????.")
                routeAction.popupBackStack()
            }
        }
    }

}

/** ?????? ?????? **/
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
            titleText = "?????? ??????",
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

/** ?????? ?????? **/
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun CardRegistrationBody(
    pagerState: PagerState,
    routeAction: RouteAction,
    tabItems: List<String>,
    viewModel: CardRegistrationViewModel,
    modalState: ModalBottomSheetState
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
                    CardCoupon(routeAction, modalState, viewModel)
                }
            }
        }
    }
}

/** ???????????? ?????? **/
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
                hint = "????????? ?????? 20??? (??????)",
                imeAction = ImeAction.Next
            )
            Text(
                text = "???????????? ????????? ??? ???????????? ???????????????.",
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
            hint = "???????????? ???????????? 16?????? (??????)",
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
            hint = "Pin?????? 8?????? (??????)",
            keyboardType = KeyboardType.Number
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGray)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("???????????? ?????? ?????? ???, ?????? ????????? ?????? ????????? ?????? ?????? ???????????????.\n\n????????? ????????? e-Gift Card??? ")
                    withStyle(SpanStyle(color = MainColor, fontWeight = FontWeight.Bold)) {
                        append("????????? ????????????")
                    }
                    append("??? ??????????????????. ???????????? ????????? ??? ???????????? ???????????????.")
                },
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }

}

/** ?????? ????????? **/
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardCoupon(
    routeAction: RouteAction,
    modalState: ModalBottomSheetState,
    viewModel: CardRegistrationViewModel
) {
    val scope = rememberCoroutineScope()

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
                text = "????????? ?????? ???????????? ???????????? ???????????? ????????? ???????????????",
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.padding(vertical = 9.dp, horizontal = 22.dp)
            )
        }
        CardCouponItem(
            imageRes = R.drawable.ic_image,
            text = "????????? ????????? ????????????",
            onClickListener = {}
        )
        CardCouponItem(
            imageRes = R.drawable.ic_barcode,
            text = "????????? ????????????",
            onClickListener = {
                viewModel.event(CardRegistrationEvent.ModalChange(1))
                scope.launch {
                    modalState.show()
                }
            }
        )
        CardCouponItem(
            imageRes = R.drawable.ic_password,
            text = "????????? ?????? ????????????",
            onClickListener = {
                viewModel.event(CardRegistrationEvent.ModalChange(2))
                scope.launch {
                    modalState.show()
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(LightGray)
        ) {
            Text(
                text = "????????? ?????? ????????????????",
                style = getTextStyle(14, true),
                modifier = Modifier.padding(top = 27.dp, start = 23.dp, end = 23.dp)
            )
            Text(
                text = "???????????? ???????????? ??? ???????????? ???????????? ????????? ???????????????, ???????????? ????????? ???????????? ???????????? ??? ????????????.\n\n" +
                        "???????????? ???????????? ???????????? ????????? ????????? ????????? ???????????? ????????? ?????? ????????????.\n\n" +
                        "?????? ????????? ?????? ????????? ?????? ????????? ?????? ????????? 7??? ????????? ???????????????.",
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

/** ?????? ?????? **/
@Composable
fun CardRegistrationFooter(viewModel: CardRegistrationViewModel) {
    Surface(
        elevation = 6.dp,
        color = White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomCheckBox(
                text = "???????????? ?????? ???????????? [??????]",
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
                text = "????????????",
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