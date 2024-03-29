package com.example.starbucksclone.view.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.BorderColor
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RouteAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    routeAction: RouteAction,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()
    val status = viewModel.status.collectAsState().value
    val isShow = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            stickyHeader {
                /** 타이틀 영역 **/
                MainTitle(
                    titleText = stringResource(id = R.string.login),
                    isExpand = state.isScrolled.not(),
                    onLeftIconClick = {
                        routeAction.popupBackStack()
                    }
                )
            }
            item {
                /** 로그인 안내 영역 **/
                LoginGuideArea()
                /** 로그인 영역 **/
                LoginArea(
                    viewModel = viewModel,
                    routeAction = routeAction,
                    modifier = Modifier.wrapContentHeight()
                )
            }
        }
        /** 풋터 영역 **/
        FooterWithButton(
            text = stringResource(id = R.string.do_login),
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.event(LoginEvent.Login)
        }
    }

    when(status) {
        is LoginViewModel.LoginStatus.Init -> {}
        is LoginViewModel.LoginStatus.Success -> {
            routeAction.goToMain()
        }
        is LoginViewModel.LoginStatus.Failure -> {
            isShow.value = true
        }
    }

    CommonTitleDialog(
        title = stringResource(id = R.string.id_or_password_check),
        isShow = isShow.value,
        okText = stringResource(id = R.string.ok),
        okClickListener = {
            isShow.value = false
        },
        isCancelVisible = false
    )
}

/** 로그인 안내 영역 **/
@Composable
fun LoginGuideArea() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "logo",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(top = 60.dp, start = 23.dp)
                .size(60.dp)
        )

        Text(
            text = stringResource(id = R.string.hello_starbucks),
            style = getTextStyle(22, true),
            modifier = Modifier.padding(top = 27.dp, start = 22.dp)
        )

        Text(
            text = stringResource(id = R.string.login_guide),
            style = getTextStyle(14),
            modifier = Modifier.padding(top = 10.dp, start = 22.dp)
        )
    }
}

/** 로그인 영역 **/
@Composable
fun LoginArea(
    viewModel: LoginViewModel,
    routeAction: RouteAction,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 80.dp)
    ) {
        CommonTextField(
            value = viewModel.id.value,
            onValueChange = {
                viewModel.event(LoginEvent.IdChange(it))
            },
            hint = stringResource(id = R.string.id),
            isLabel = true,
            imeAction = ImeAction.Next
        )

        CommonTextField(
            value = viewModel.password.value,
            onValueChange = {
                viewModel.event(LoginEvent.PasswordChange(it))
            },
            hint = stringResource(id = R.string.password),
            isLabel = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.find_id),
                style = getTextStyle(12),
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 9.dp)
                    .background(BorderColor)
            )
            Text(
                text = stringResource(id = R.string.find_password),
                style = getTextStyle(12),
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 9.dp)
                    .background(BorderColor)
            )
            Text(
                text = stringResource(id = R.string.signup),
                style = getTextStyle(12),
                modifier = Modifier
                    .nonRippleClickable {
                        routeAction.goToScreen(RouteAction.Terms)
                    }
            )
        }
    }
}
