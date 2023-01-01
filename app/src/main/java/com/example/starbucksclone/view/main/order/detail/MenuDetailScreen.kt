package com.example.starbucksclone.view.main.order.detail

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.main.order.cart.CartAdditionCompleteBottomSheet
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDetailScreen(
    routeAction: RouteAction,
    viewModel: MenuDetailViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    val modalState = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        isSkipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val status = viewModel.status.collectAsState().value
    val context = LocalContext.current

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            when (status) {
                is MenuDetailViewModel.MenuDetailStatus.CartAdditionSuccess -> {
                    CartAdditionCompleteBottomSheet(
                        goToCartListener = {
                            routeAction.goToScreen(RouteAction.Cart)
                        },
                        finishListener = {
                            routeAction.popupBackStack()
                        },
                        closeListener = {
                            scope.launch {
                                modalState.hide()
                            }
                        }
                    )
                }
                else -> {
                    OrderInfoBottomSheetContents(
                        id = viewModel.id ?: "",
                        info = viewModel.info.value,
                        isHot = when (viewModel.info.value.drinkType) {
                            Constants.HotOnly -> {
                                true
                            }
                            Constants.IcedOnly -> {
                                false
                            }
                            else -> {
                                viewModel.isHotSelect.value
                            }
                        },
                        heartClickListener = {
                            viewModel.event(MenuDetailEvent.MyMenuRegister(it))
                        },
                        cartClickListener = {
                            scope.launch {
                                modalState.hide()
                            }
                            viewModel.event(MenuDetailEvent.AddCartItem(it))
                        },
                        orderClickListener = {
                            routeAction.goToPayment(Uri.encode(Gson().toJson(it)))
                        }
                    )
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        /** 상단 이미지 **/
                        AsyncImage(
                            model = viewModel.info.value.image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(getColorFromHexCode(viewModel.info.value.color))
                                .height(270.dp)
                        )

                    }
                    item {
                        /** 바디 영역 **/
                        MenuDetailBody(
                            info = viewModel.info.value,
                            isHotSelected = viewModel.isHotSelect.value,
                            isOnly = viewModel.info.value.drinkType.contains("ONLY")
                        ) { isHot ->
                            viewModel.event(MenuDetailEvent.HotIcedChange(isHot))
                        }
                    }
                }

                /** 해더 영역 **/
                MenuDetailHeader(
                    routeAction = routeAction,
                    isExpand = state.firstVisibleItemIndex > 1,
                    name = viewModel.info.value.name
                )
            }
            /** 풋터 영역 **/
            FooterWithButton(text = "주문하기") {
                scope.launch {
                    modalState.show()
                }
            }
        }
    }

    when (status) {
        is MenuDetailViewModel.MenuDetailStatus.Init -> {}
        is MenuDetailViewModel.MenuDetailStatus.Failure -> {
            context.toast(status.msg)
        }
        is MenuDetailViewModel.MenuDetailStatus.MyMenuSuccess -> {
            context.toast("나만의 메뉴 등록을 완료하였습니다.")
        }
        is MenuDetailViewModel.MenuDetailStatus.CartAdditionSuccess -> {
            scope.launch {
                modalState.show()
            }
        }
    }
}

/** 해더 영역 **/
@Composable
fun MenuDetailHeader(
    routeAction: RouteAction,
    isExpand: Boolean,
    name: String
) {
    when (isExpand) {
        true -> {
            Surface(
                elevation = 6.dp,
                color = White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .nonRippleClickable { routeAction.popupBackStack() }
                    )
                    Text(
                        text = name,
                        style = if (isExpand) Typography.subtitle1 else Typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "share",
                        modifier = Modifier.padding(end = 17.dp)
                    )
                }
            }
        }
        false -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp, start = 12.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(Color(0x33000000))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back",
                        tint = White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .nonRippleClickable { routeAction.popupBackStack() }
                            .padding(end = 1.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp, end = 12.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(Color(0x33000000))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "share",
                        tint = White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

/** 바디 영역 **/
@Composable
fun MenuDetailBody(
    info: MenuDetailInfo,
    isHotSelected: Boolean,
    isOnly: Boolean,
    hotIcedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    ) {
        Text(
            text = info.name,
            style = getTextStyle(24, true, Black),
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = info.nameEng,
            style = getTextStyle(12, false, DarkGray),
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = info.description,
            style = getTextStyle(12, false, Black),
            modifier = Modifier.padding(top = 15.dp)
        )

        Text(
            text = info.sizePrices.getOrNull(0)?.toPriceFormat() ?: "0원",
            style = getTextStyle(20, true, Black),
            modifier = Modifier.padding(top = 20.dp)
        )

        HotOrIcedSelector(
            isHotSelected = isHotSelected,
            isOnly = isOnly,
            drinkType = info.drinkType,
            modifier = Modifier.padding(top = 22.dp)
        ) {
            hotIcedChange(it)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = "제품 영양 정보",
                style = getTextStyle(16, true, Black),
                modifier = Modifier.weight(1f)
            )
            Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = null)
        }

    }
}

@Composable
fun HotOrIcedSelector(
    isHotSelected: Boolean,
    isOnly: Boolean = false,
    drinkType: String,
    modifier: Modifier = Modifier,
    selectedListener: (Boolean) -> Unit
) {
    if (isOnly) {
        RoundedButton(
            text = if (drinkType == Constants.HotOnly) Constants.HotOnly else Constants.IcedOnly,
            isOutline = true,
            buttonColor = DarkGray,
            textStyle = getTextStyle(14, true),
            textColor = if (drinkType == Constants.HotOnly) HotColor else IceColor,
            modifier = modifier
                .fillMaxWidth()
                .height(35.dp)
        ) {

        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .border(BorderStroke(1.dp, DarkGray), RoundedCornerShape(17.dp))
        ) {
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .background(if (isHotSelected) HotColor else White)
                    .nonRippleClickable { selectedListener(true) }
            ) {
                Text(
                    text = Constants.Hot,
                    style = getTextStyle(14, true, if (isHotSelected) White else DarkGray),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
                    .background(if (isHotSelected) White else IceColor)
                    .nonRippleClickable { selectedListener(false) }
            ) {
                Text(
                    text = Constants.Iced,
                    style = getTextStyle(14, true, if (isHotSelected) DarkGray else White),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
