package com.example.starbucksclone.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.Title
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginScreen(routAction: RoutAction) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.White)
    systemUiController.statusBarDarkContentEnabled = true

    val lazyListSate = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        /** 타이틀 **/
        Title(
            leftIconRes = R.drawable.ic_back,
            onLeftIconClick = { routAction.popupBackStack() },
            lazyListSate = lazyListSate,
            title = "로그인"
        )
        /** 바디 **/
        LoginBody(
            lazyListSate = lazyListSate,
            routAction = routAction,
            modifier = Modifier
                .weight(1f)
        )
        /** 풋터 **/
        FooterWithButton(text = "로그인하기") {

        }
    }
}

/** 로그인 바디 **/
@Composable
fun LoginBody(
    lazyListSate: LazyListState,
    routAction: RoutAction,
    modifier: Modifier = Modifier
) {
    val tempIdState = remember { mutableStateOf("") }
    val tempPwState = remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(top = 41.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
        state = lazyListSate,
        modifier = modifier
    ) {

        // 타이틀
        item {
            Text(text = if (lazyListSate.isScrolled) "" else "로그인", style = Typography.subtitle1)
        }
        // 로고
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 28.dp)
                    .size(60.dp)
            )
        }
        // 안내 문구
        item {
            Text(text = "안녕하세요.\n스타벅스입니다.", style = Typography.subtitle1)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "회원 서비스 이용을 위해 로그인 해주세요.", style = Typography.body1)
        }
        // 아이디 / 비밀번호 입력창
        item {
            CommonTextField(
                value = tempIdState.value,
                onValueChange = {
                    tempIdState.value = it
                },
                hint = "아이디",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 84.dp)
            )

            CommonTextField(
                value = tempPwState.value,
                onValueChange = {
                    tempPwState.value = it
                },
                hint = "비밀번호",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            )
        }
        // 아이디 찾기 / 비빌번호 찾기 / 회원가입 선택
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(33.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "아이디 찾기", style = Typography.caption)
                Box(
                    modifier = Modifier
                        .size(height = 8.dp, width = 2.dp)
                        .background(Gray)
                )
                Text(text = "비밀번호 찾기", style = Typography.caption)
                Box(
                    modifier = Modifier
                        .size(height = 8.dp, width = 2.dp)
                        .background(Gray)
                )
                Text(
                    text = "회원가입",
                    style = Typography.caption,
                    modifier = Modifier
                        .clickable { routAction.goToTerms() }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
