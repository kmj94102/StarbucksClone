package com.example.starbucksclone.view.main.pay.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.view.common.MotionTitle
import com.example.starbucksclone.view.navigation.RoutAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardListScreen(
    routAction: RoutAction,
    viewModel: CardListViewModel = hiltViewModel()
) {
    val state = LazyListState()

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            MotionTitle(
                rightIconRes = R.drawable.ic_plus_circle,
                onLeftIconClick = {},
                onRightIconClick = {},
                titleText = "카드",
            )
        }

        item {
            viewModel.cardList.forEachIndexed { index, cardListInfo ->
                when (index) {
                    0 -> {
                        PayCardFirstItem(cardListInfo = cardListInfo) {
                            routAction.goToCardDetail(it)
                        }
                    }
                    else -> {
                        PayCardItem(cardListInfo = cardListInfo) {
                            routAction.goToCardDetail(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PayCardFirstItem(
    cardListInfo: CardInfo,
    onClickListener: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
            .nonRippleClickable {
                onClickListener(cardListInfo.cardNumber)
            }
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = cardListInfo.image,
                contentDescription = "",
                modifier = Modifier.size(115.dp, 73.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 13.dp)
            ) {
                Text(
                    text = cardListInfo.name,
                    style = Typography.body1,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = cardListInfo.balance.toPriceFormat(),
                    style = Typography.subtitle2,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_star_circle_selected),
                contentDescription = "",
                modifier = Modifier
                    .size(41.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
    }
}

@Composable
fun PayCardItem(
    cardListInfo: CardInfo,
    onClickListener: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
            .nonRippleClickable {
                onClickListener(cardListInfo.cardNumber)
            }
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = cardListInfo.image,
                contentDescription = "",
                modifier = Modifier.size(56.dp, 35.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 13.dp)
            ) {
                Text(
                    text = cardListInfo.name,
                    style = Typography.caption,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = cardListInfo.balance.toPriceFormat(),
                    style = Typography.body1,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_star_circle),
                contentDescription = "",
                modifier = Modifier
                    .size(41.dp)
                    .align(Alignment.CenterVertically)
            )

        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
    }
}