package com.example.starbucksclone.view.main.pay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.MotionTitle
import com.example.starbucksclone.view.navigation.RoutAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PayScreen(routAction: RoutAction) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 30.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        stickyHeader {
            MotionTitle(
                leftIconRes = null,
                lazyListSate = listState,
                titleText = "Pay"
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            EmptyPayCard {
                routAction.goToCardRegistration()
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Banner()
        }
    }
}

@Composable
fun EmptyPayCard(onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(445.dp)
            .padding(horizontal = 10.dp)
            .nonRippleClickable {
                onClick()
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_card),
                contentDescription = "empty",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(width = 263.dp, height = 167.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "스타벅스 카드를 등록해보세요.",
                style = Typography.subtitle2,
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "매장과 사이렌오더에서 쉽고 편리하게\n사용할 수 있고, 별도 적립할 수 있습니다.",
                style = Typography.body1,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun Banner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFFFFF7E6))
    )
}