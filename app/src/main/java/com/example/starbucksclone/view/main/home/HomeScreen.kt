package com.example.starbucksclone.view.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.view.common.RoundedButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Spacer(modifier = Modifier.height(52.dp))
            Text(
                text = "안녕하세요.\n스타벅스입니다.",
                style = Typography.subtitle1,
                modifier = Modifier.padding(start = 23.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))

            ElevatedCard(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(
                        top = 35.dp,
                        bottom = 35.dp,
                        start = 26.dp,
                        end = 12.dp
                    )
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "스타벅스 리워드\n회원 신규 가입 첫 구매 시,\n무료음료 혜택드려요!",
                            style = Typography.body1,
                            lineHeight = 28.sp,
                            color = DarkGray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row {
                            RoundedButton(
                                text = "회원가입",
                            ) {

                            }
                            RoundedButton(
                                text = "로그인",
                                isOutline = true,
                                textColor = MainColor,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            ) {

                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.img_reward),
                        contentDescription = "reward",
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(width = 109.dp, height = 112.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        stickyHeader {
            Row(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 23.dp, vertical = 15.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_messgae),
                    contentDescription = "message"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "What's New", style = Typography.body2)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = "bell",
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillParentMaxSize()
                    .background(DarkGray)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillParentMaxSize()
                    .background(MainColor)
            )
        }
    }
}