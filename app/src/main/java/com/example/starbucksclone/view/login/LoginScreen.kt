package com.example.starbucksclone.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.Title
import com.example.starbucksclone.view.navigation.RoutAction
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginScreen(
    routAction: RoutAction,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val lazyListSate = rememberLazyListState()
    val context = LocalContext.current

    val status = viewModel.status.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        /** 타이틀 영역 **/
        Title(
            leftIconRes = R.drawable.ic_back,
            onLeftIconClick = { routAction.popupBackStack() },
            lazyListSate = lazyListSate,
            title = "로그인"
        )
        /** 바디 영역 **/
        LoginBody(
            lazyListSate = lazyListSate,
            routAction = routAction,
            viewModel = viewModel,
            modifier = Modifier
                .weight(1f)
        )
        /** 풋터 영역 **/
        FooterWithButton(text = "로그인하기") {
            val loginInfo = viewModel.loginInfo.value
            if (loginInfo.id.isEmpty() || loginInfo.password.isEmpty()) {
                context.toast("아이디 또는 패스워드를 입력해주세요")
            }
            viewModel.event(LoginEvent.Login)
        }
    }

    when(status.value) {
        is LoginViewModel.Status.Init -> {}
        is LoginViewModel.Status.Failure -> {
            context.toast("아이디 또는 패스워드를 확인해주세요")
        }
        is LoginViewModel.Status.Success -> {
            context.toast("로그인 성공")
        }
    }
}

/** 로그인 바디 **/
@Composable
fun LoginBody(
    lazyListSate: LazyListState,
    routAction: RoutAction,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
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
                value = viewModel.loginInfo.value.id,
                onValueChange = {
                    viewModel.event(LoginEvent.IdChange(it))
                },
                imeAction = ImeAction.Next,
                isLabel = true,
                hint = "아이디",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 84.dp)
            )

            CommonTextField(
                value = viewModel.loginInfo.value.password,
                onValueChange = {
                    viewModel.event(LoginEvent.PwChange(it))
                },
                visualTransformation = PasswordVisualTransformation(),
                isLabel = true,
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
