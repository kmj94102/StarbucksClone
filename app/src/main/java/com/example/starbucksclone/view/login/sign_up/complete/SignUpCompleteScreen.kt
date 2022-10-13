package com.example.starbucksclone.view.login.sign_up.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.today
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.Title
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun SignUpCompleteScreen(
    routAction: RoutAction,
    isPush: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title(
            leftIconRes = R.drawable.ic_close,
            onLeftIconClick = {
                routAction.popupBackStack()
            }
        )

        SignUpCompleteBody(isPush = isPush, modifier = Modifier.weight(1f))

        FooterWithButton(text = "스타벅스 카드등록 바로가기") {

        }

    }
}

@Composable
fun SignUpCompleteBody(isPush: Boolean, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 27.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = "김민재님,\n회원가입이 완료되었습니다.",
                style = Typography.subtitle1,
                lineHeight = 32.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        item {
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
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "${today()} E-Mail 및 SMS\n광고성 정보 수신 여부 처리 결과",
                    style = Typography.body1,
                    color = DarkGray,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 30.dp)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(Gray)
                )
                Text(
                    text = if(isPush) "수신 동의" else "수신 거부",
                    style = Typography.body2,
                    modifier = Modifier
                        .padding(start = 30.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            Text(
                text = "제공자: (주) 스타벅스커피 코리아",
                style = Typography.caption,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_card),
                    contentDescription = "create_card",
                    modifier = Modifier
                        .size(width = 205.dp, height = 141.dp)
                )
                Text(
                    text = "스타벅스 카드를 등록하시고\n웰컴 첫 구매 쿠폰과 별 적립 혜택을 받아보세요.",
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 55.dp)
                )

            }
        }
    }
}
