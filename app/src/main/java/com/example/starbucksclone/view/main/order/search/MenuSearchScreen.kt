package com.example.starbucksclone.view.main.order.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.BorderColor
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.SearchTextField
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun MenuSearchScreen(
    routeAction: RouteAction,
    viewModel: MenuSearchViewModel = hiltViewModel()
) {
    val search = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp, vertical = 10.dp)
        ) {
            SearchTextField(
                value = search.value,
                onValueChange = {
                    search.value = it
                },
                onSearchListener = {
                    viewModel.event(MenuSearchEvent.Search(search.value))
                    routeAction.goToMenuSearchResult(search.value)
                },
                hint = "검색",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "취소",
                style = getTextStyle(14),
                modifier = Modifier
                    .padding(18.dp)
                    .nonRippleClickable { routeAction.popupBackStack() }
            )
        }

        if (viewModel.historyList.isEmpty()) {
            Text(
                text = "최근 검색어가 없습니다.",
                style = getTextStyle(16),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 170.dp)
            )
        } else {
            Text(
                text = "최근 검색어",
                style = getTextStyle(12, false, DarkGray),
                modifier = Modifier.padding(start = 23.dp, top = 20.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(bottom = 50.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                item {
                    viewModel.historyList.forEach {
                        SearchHistory(
                            text = it,
                            onClickListener = { value ->
                                viewModel.event(MenuSearchEvent.Search(value))
                                routeAction.goToMenuSearchResult(value)
                            },
                            deleteListener = { value ->
                                viewModel.event(MenuSearchEvent.DeleteHistory(value))
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 23.dp, vertical = 10.dp)
                            .height(1.dp)
                            .background(Gray)
                    )
                    Text(
                        text = "전체 삭제",
                        textAlign = TextAlign.End,
                        style = getTextStyle(12, true),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 23.dp)
                            .nonRippleClickable {
                                viewModel.event(MenuSearchEvent.AllDelete)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchHistory(
    text: String,
    onClickListener: (String) -> Unit,
    deleteListener: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            style = getTextStyle(14),
            modifier = Modifier
                .weight(1f)
                .nonRippleClickable { onClickListener(text) }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "",
            modifier = Modifier
                .size(18.dp)
                .nonRippleClickable { deleteListener(text) }
        )
    }
}