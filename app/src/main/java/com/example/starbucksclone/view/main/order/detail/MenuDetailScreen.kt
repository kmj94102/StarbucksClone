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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CartEntity
import com.example.starbucksclone.database.entity.MenuDetailInfo
import com.example.starbucksclone.database.entity.MyMenuEntity
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.RoundedButton
import com.example.starbucksclone.view.main.order.cart.CartAdditionCompleteBottomSheet
import com.example.starbucksclone.view.navigation.RouteAction
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
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
            MenuDetailModalContainer(
                status = status,
                viewModel = viewModel,
                scope = scope,
                modalState = modalState,
                routeAction = routeAction
            )
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
                            isOnly = viewModel.info.value.drinkType.contains("ONLY"),
                            group = viewModel.group.value
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
            if (viewModel.group.value == Constants.Drink) {
                FooterWithButton(text = stringResource(id = R.string.do_order)) {
                    scope.launch {
                        modalState.show()
                    }
                }
            } else {
                OrderInfoFooter(
                    info = viewModel.info.value,
                    routeAction = routeAction,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    when (status) {
        is MenuDetailViewModel.MenuDetailStatus.Init -> {}
        is MenuDetailViewModel.MenuDetailStatus.Failure -> {
            context.toast(status.msg)
            viewModel.event(MenuDetailEvent.StatusInit)
        }
        is MenuDetailViewModel.MenuDetailStatus.MyMenuSuccess -> {
            context.toast(R.string.my_menu_add_complete)
            viewModel.event(MenuDetailEvent.StatusInit)
        }
        is MenuDetailViewModel.MenuDetailStatus.CartAdditionSuccess -> {
            scope.launch {
                modalState.show()
            }
            viewModel.event(MenuDetailEvent.StatusInit)
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
    group: String,
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

        if (group == Constants.Drink) {
            HotOrIcedSelector(
                isHotSelected = isHotSelected,
                isOnly = isOnly,
                drinkType = info.drinkType,
                modifier = Modifier.padding(top = 22.dp)
            ) {
                hotIcedChange(it)
            }
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
                text = stringResource(id = R.string.nutritional_information),
                style = getTextStyle(16, true, Black),
                modifier = Modifier.weight(1f)
            )
            Image(painter = painterResource(id = R.drawable.ic_next), contentDescription = null)
        }

    }
}

/** 아이스 / 핫 선택 영역 **/
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
        ) {}
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

/** 풋터 영역 **/
@Composable
private fun OrderInfoFooter(
    info: MenuDetailInfo,
    modifier: Modifier = Modifier,
    routeAction: RouteAction,
    viewModel: MenuDetailViewModel
) {
    val amount = remember {
        mutableStateOf(0)
    }
    val isShow = remember {
        mutableStateOf(false)
    }

    Surface(
        elevation = 6.dp,
        color = White,
        modifier = modifier
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (minus, plus, count, priceText, heart, cart, order) = createRefs()

            Icon(
                painter = painterResource(id = R.drawable.ic_minus_circle),
                contentDescription = "minus",
                tint = if (amount.value <= 1) Gray else Color(0xFF585858),
                modifier = Modifier
                    .constrainAs(minus) {
                        top.linkTo(count.top)
                        bottom.linkTo(priceText.bottom)
                        start.linkTo(parent.start, 20.dp)
                    }
                    .nonRippleClickable {
                        if (amount.value > 1) {
                            amount.value = amount.value - 1
                        }
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_plus_circle),
                contentDescription = "plus",
                tint = if (amount.value > 100) Gray else Color(0xFF585858),
                modifier = Modifier
                    .constrainAs(plus) {
                        top.linkTo(count.top)
                        bottom.linkTo(count.bottom)
                        start.linkTo(count.end)
                    }
                    .nonRippleClickable {
                        if (amount.value < 100) {
                            amount.value = amount.value + 1
                        }
                    }
            )
            Text(
                text = "${amount.value}",
                style = getTextStyle(20),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(count) {
                        top.linkTo(parent.top, 17.dp)
                        start.linkTo(minus.end)
                        end.linkTo(plus.start)
                        width = Dimension.value(55.dp)
                    }
            )

            Text(
                text = (info.sizePrices.getOrElse(0) { 0 } * amount.value).toPriceFormat(),
                style = getTextStyle(20, true),
                modifier = Modifier.constrainAs(priceText) {
                    top.linkTo(parent.top, 22.dp)
                    end.linkTo(parent.end, 20.dp)
                }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_empty_heart),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(heart) {
                        bottom.linkTo(parent.bottom, 18.dp)
                        start.linkTo(parent.start, 23.dp)
                    }
                    .nonRippleClickable {
                        isShow.value = true
                    }
            )

            RoundedButton(
                text = stringResource(id = R.string.do_cart),
                isOutline = true,
                textColor = MainColor,
                onClick = {
                    viewModel.event(
                        MenuDetailEvent.AddCartItem(
                            createCartItem(
                                id = viewModel.id ?: "",
                                info = info,
                                amount = amount.value
                            )
                        )
                    )
                },
                modifier = Modifier
                    .constrainAs(cart) {
                        top.linkTo(order.top)
                        bottom.linkTo(order.bottom)
                        end.linkTo(order.start, 12.dp)
                    }
            )

            RoundedButton(
                text = stringResource(id = R.string.do_order),
                onClick = {
                    routeAction.goToPayment(
                        Uri.encode(
                            Gson().toJson(
                                createCartItem(
                                    id = viewModel.id ?: "",
                                    info = info,
                                    amount = amount.value
                                ).paymentInfoMapper()
                            )
                        )
                    )
                },
                modifier = Modifier.constrainAs(order) {
                    top.linkTo(priceText.bottom, 15.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, 20.dp)
                }
            )
        }
    }

    /** 나만의 메뉴 등록 다이얼로그 **/
    MyMenuRegisterDialog(
        isShow = isShow.value,
        name = info.name,
        property = "",
        okClickListener = {
            isShow.value = false
            viewModel.event(
                MenuDetailEvent.MyMenuRegister(
                    MyMenuEntity(
                        id = viewModel.id ?: "",
                        menuIndex = info.index,
                        name = info.name,
                        nameEng = info.nameEng,
                        image = info.image,
                        anotherName = it,
                        price = info.sizePrices.getOrElse(0) { 0 }.toInt(),
                        property = "",
                        date = System.currentTimeMillis()
                    )
                )
            )

        },
        cancelClickListener = {
            isShow.value = false
        }
    )
}

/** 장바구니 아이템 생성 **/
private fun createCartItem(
    id: String,
    info: MenuDetailInfo,
    amount: Int
) = CartEntity(
    id = id,
    menuIndex = info.index,
    name = info.name,
    nameEng = info.nameEng,
    property = "",
    image = info.image,
    price = info.sizePrices.getOrElse(0) { 0 }.toInt(),
    amount = amount,
    date = System.currentTimeMillis()
)

/** 바텀시트 다이얼로 **/
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuDetailModalContainer(
    status: MenuDetailViewModel.MenuDetailStatus,
    viewModel: MenuDetailViewModel,
    scope: CoroutineScope,
    modalState: ModalBottomSheetState,
    routeAction: RouteAction
) {
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
                    routeAction.goToPayment(Uri.encode(Gson().toJson(it.paymentInfoMapper())))
                }
            )
        }
    }
}
