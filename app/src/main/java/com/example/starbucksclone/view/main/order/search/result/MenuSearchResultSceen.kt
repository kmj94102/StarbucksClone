package com.example.starbucksclone.view.main.order.search.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.MenuSearchResult
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun MenuSearchResultScreen(
    routeAction: RouteAction,
    viewModel: MenuSearchResultViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        /** 해더 영역 **/
        MenuSearchResultHeader(
            viewModel = viewModel,
            routeAction = routeAction
        )
        /** 검색 기록이 없을 경우 **/
        if (viewModel.list.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.empty_search_result),
                style = getTextStyle(16),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            /** 바디 영역 **/
            MenuSearchResultBody(
                list = viewModel.list,
                routeAction = routeAction,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/** 해더영역 **/
@Composable
fun MenuSearchResultHeader(
    viewModel: MenuSearchResultViewModel,
    routeAction: RouteAction,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MainTitle(
            titleText = viewModel.title.value,
            isExpand = false,
            onLeftIconClick = {
                routeAction.popupBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(24.dp))
            listOf(
                stringResource(id = R.string.all),
                stringResource(id = R.string.drink),
                stringResource(id = R.string.food),
                stringResource(id = R.string.product)
            ).forEach {
                Text(
                    text = it,
                    style = getTextStyle(
                        size = 14,
                        isBold = it == viewModel.selected.value,
                        color = if (it == viewModel.selected.value) Black else DarkGray
                    ),
                    modifier = Modifier
                        .nonRippleClickable {
                            viewModel.event(MenuSearchResultEvent.SelectedChange(it))
                        }
                        .padding(horizontal = 16.dp, vertical = 17.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
    }
}

/** 바디 영역 **/
@Composable
fun MenuSearchResultBody(
    list: List<MenuSearchResult>,
    routeAction: RouteAction,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 30.dp, bottom = 100.dp),
        modifier = modifier
    ) {
        item {
            list.forEach {
                MenuListItem(
                    menu = it
                ) { indexes, name, group ->
                    routeAction.goToMenuDetail(indexes, name, group)
                }
            }
        }
    }
}

/** 검색 결과 아이템 **/
@Composable
fun MenuListItem(
    menu: MenuSearchResult,
    onClick: (String, String, String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .nonRippleClickable { onClick(menu.indexes, menu.name, menu.group) }
            .padding(bottom = 23.dp)
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        CircleImage(
            imageURL = menu.image,
            size = 96.dp
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = menu.name, style = getTextStyle(14, true, Black))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = menu.nameEng, style = getTextStyle(12, false, DarkGray))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = menu.price.priceFormat(), style = getTextStyle(14, true, Black))
        }
        Spacer(modifier = Modifier.width(24.dp))
    }
}