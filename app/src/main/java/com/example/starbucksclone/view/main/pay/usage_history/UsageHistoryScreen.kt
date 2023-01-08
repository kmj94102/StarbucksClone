package com.example.starbucksclone.view.main.pay.usage_history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.UsageHistoryInfo
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.util.dateFormat
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun UsageHistoryScreen(
    routeAction: RouteAction,
    viewModel: UsageHistoryViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MainTitle(
            titleText = stringResource(id = R.string.usage_history),
            isExpand = state.isScrolled.not(),
            onLeftIconClick = { routeAction.popupBackStack() }
        )
        if (viewModel.list.isEmpty()) {
            /** 이용 내역이 없을 때 바디 영역 **/
            UsageHistoryEmptyBody(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                viewModel.list.forEach {
                    item {
                        /** 이용 내역 바디 영역 아이템 **/
                        UsageHistoryBodyItem(it)
                    }
                }
            }
        }
    }
}

/** 이용 내역이 없을 때 바디 영역 **/
@Composable
fun UsageHistoryEmptyBody(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.usage_history_empty),
            style = getTextStyle(20, true)
        )
    }
}

/** 이용 내역 바디 영역 아이템 **/
@Composable
fun UsageHistoryBodyItem(
    usageHistoryInfo: UsageHistoryInfo
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_coffee),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 28.dp, start = 27.dp)
                    .size(30.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 23.dp)
            ) {
                Text(
                    text = usageHistoryInfo.name,
                    style = getTextStyle(14, true)
                )
                Text(
                    text = usageHistoryInfo.date.dateFormat(),
                    style = getTextStyle(12, false, DarkGray),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Text(
                text = "-${usageHistoryInfo.amount * usageHistoryInfo.price}",
                style = getTextStyle(14, true, Color(0xFF9B8756)),
                modifier = Modifier.padding(end = 55.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 23.dp)
                .background(LightGray)
        )
    }
}