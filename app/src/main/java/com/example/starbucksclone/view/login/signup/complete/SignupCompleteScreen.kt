package com.example.starbucksclone.view.login.signup.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.navigation.RouteAction
import kotlinx.coroutines.delay

@Composable
fun SignupCompleteScreen(
    routeAction: RouteAction,
    viewModel: SignupCompleteViewModel = hiltViewModel()
) {
    val status = viewModel.status.collectAsState().value
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        /** 해더 영역 **/
        SignupCompleteHeader(
            routeAction = routeAction,
            name = viewModel.name.value,
            isPush = viewModel.isPushConsent.value,
        )
        /** 바디 영역 **/
        SignupCompleteBody(
            modifier = Modifier.weight(1f)
        )
        /** 풋터 영역 **/
        SignupCompleteFooter(routeAction = routeAction)
    }
    
    BackPressHandler {
        routeAction.goToMain()
    }

    if (status == SignupCompleteViewModel.SignupCompleteStatus.Error) {
        LaunchedEffect(true) {
            context.toast("회원가입 중 오류가 발생하였습니다.")
            delay(1500)
            routeAction.popupBackStack()
        }
    }
}

/** 해더 영역 **/
@Composable
fun SignupCompleteHeader(
    routeAction: RouteAction,
    name: String,
    isPush: Boolean,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_close),
        contentDescription = "close",
        modifier = Modifier
            .padding(top = 9.dp, start = 16.dp)
            .nonRippleClickable {
                routeAction.goToMain()
            }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "${name}님,\n회원가입이 완료되었습니다.", style = getTextStyle(24))
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
                text = "${today()} E-Mail 및 SMS 광고성 정보 수신여부 처리 결과",
                style = getTextStyle(size = 14, color = DarkGray),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 30.dp)
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 30.dp)
                    .background(Gray)
            )
            Text(
                text = if (isPush) "수신 동의" else "수신 거부",
                style = getTextStyle(14),
                modifier = Modifier.padding(start = 30.dp)
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
            style = getTextStyle(size = 12, color = DarkGray),
            modifier = Modifier
                .padding(top = 6.dp)
                .align(Alignment.End)
        )
    }
}

/** 바디 영역 **/
@Composable
fun SignupCompleteBody(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.img_card),
            contentDescription = null,
            modifier = Modifier
                .size(205.dp, 141.dp)
        )
        Text(
            text = "스타벅스 카드를 등록하시고\n웰컴 첫 구매 쿠폰과 별 적립 혜택을 받아보세요.",
            style = getTextStyle(14),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 50.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

/** 풋터 영역 **/
@Composable
fun SignupCompleteFooter(
    routeAction: RouteAction
) {
    FooterWithButton(text = "스타벅스 카드등록 바로가기") {
        routeAction.goToMain()
    }
}