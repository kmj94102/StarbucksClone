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
import com.example.starbucksclone.R
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
fun MenuListScreen(routeAction: RouteAction) {
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        stickyHeader {
            MenuListHeader(routeAction = routeAction, isExpand = state.isScrolled.not())
        }
        item {
            Spacer(modifier = Modifier.height(35.dp))
            MenuListBody(routeAction = routeAction)
        }
    }
}

@Composable
fun MenuListHeader(
    routeAction: RouteAction,
    isExpand: Boolean
) {
    MainTitle(
        titleText = "New",
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

@Composable
fun MenuListBody(routeAction: RouteAction) {
    (0..10).forEach { _ ->
        MenuListItem {
            routeAction.goToScreen(RouteAction.MenuDetail)
        }
    }
}

@Composable
fun MenuListItem(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .nonRippleClickable { onClick() }
            .padding(bottom = 23.dp)
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        CircleImage(
            imageURL = "https://image.istarbucks.co.kr/upload/store/skuimg/2022/10/[9200000002259]_20221007082850170.jpg",
            size = 96.dp
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "슈크림 라떼", style = getTextStyle(14, true, Black))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Choux Cream Latte", style = getTextStyle(12, false, DarkGray))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = 6100.priceFormat(), style = getTextStyle(14, true, Black))
        }
        Spacer(modifier = Modifier.width(24.dp))
    }
}