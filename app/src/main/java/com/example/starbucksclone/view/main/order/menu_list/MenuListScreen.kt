package com.example.starbucksclone.view.main.order.menu_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.database.entity.MenuInfo
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.CircleImage
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuListScreen(
    routeAction: RouteAction,
    viewModel: MenuListViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        stickyHeader {
            /** 해더 영역 **/
            MenuListHeader(
                title = viewModel.name.value,
                routeAction = routeAction,
                isExpand = state.isScrolled.not()
            )
        }
        item {
            /** 바디 영역 **/
            Spacer(modifier = Modifier.height(35.dp))
            MenuListBody(
                routeAction = routeAction,
                list = viewModel.list
            )
        }
    }
}

/** 해더 영역 **/
@Composable
fun MenuListHeader(
    title: String,
    routeAction: RouteAction,
    isExpand: Boolean
) {
    MainTitle(
        titleText = title,
        isExpand = isExpand,
        onLeftIconClick = {
            routeAction.popupBackStack()
        },
        rightContents = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 17.dp)
                    .nonRippleClickable { }
            )
        }
    )
}

/** 바디 영역 **/
@Composable
fun MenuListBody(
    routeAction: RouteAction,
    list: List<MenuInfo>
) {
    list.forEach {
        MenuListItem(it) { indexes, name, group ->
            routeAction.goToMenuDetail(indexes, name, group)
        }
    }
}

@Composable
fun MenuListItem(
    menu: MenuInfo,
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