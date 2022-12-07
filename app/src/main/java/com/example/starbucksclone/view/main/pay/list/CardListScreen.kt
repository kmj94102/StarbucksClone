package com.example.starbucksclone.view.main.pay.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.CardItem
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardListScreen(
    routeAction: RouteAction,
    viewModel: CardListViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        /** 해더 영역 **/
        stickyHeader {
            CardListHeader(
                isExpand = state.isScrolled.not(),
                routeAction = routeAction
            )
        }
        /** 바디 영역 **/
        item {
            CardListBody(
                viewModel = viewModel,
                routeAction = routeAction
            )
        }
    }
}

/** 해더 영역 **/
@Composable
fun CardListHeader(
    isExpand: Boolean,
    routeAction: RouteAction
) {
    MainTitle(
        titleText = "카드",
        onLeftIconClick = {
            routeAction.popupBackStack()
        },
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_plus_circle),
                contentDescription = "add",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 18.dp)
                    .nonRippleClickable {
                        routeAction.goToScreen(RouteAction.CardRegistration)
                    }
            )
        },
        isExpand = isExpand,
        modifier = Modifier.fillMaxWidth()
    )
}

/** 바디 영역 **/
@Composable
fun CardListBody(
    viewModel: CardListViewModel,
    routeAction: RouteAction
) {
    val isShow = remember {
        mutableStateOf(false)
    }
    val cardName = remember {
        mutableStateOf("")
    }
    val cardNumber = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 100.dp)
    ) {
        viewModel.cardList.forEachIndexed { index, cardInfo ->
            CardItem(
                cardInfo = cardInfo,
                isBigSize = index == 0,
                isRepresentativeVisible = true,
                representativeClickListener = { number, name ->
                    isShow.value = true
                    cardName.value = name
                    cardNumber.value = number
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp, start = 23.dp, end = 18.dp)
                    .nonRippleClickable {

                    }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp)
                    .height(1.dp)
                    .background(Gray)
            )
        }
    }

    CommonTitleDialog(
        title = "[${cardName.value}]카드를 대표카드로 변경할까요?",
        contents = {
            Text(
                text = "대표카드로 설정할 경우, 리스트 최상단에 고정됩니다.",
                style = getTextStyle(14),
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        },
        isShow = isShow.value,
        cancelClickListener = { isShow.value = false },
        okClickListener = {
            isShow.value = false
            viewModel.event(CardListEvent.UpdateRepresentative(cardNumber.value))
        }
    )
}