package com.example.starbucksclone.view.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.BorderColor
import com.example.starbucksclone.util.Constants
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.common.MainTitle
import com.example.starbucksclone.view.navigation.RoutAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(routAction: RoutAction) {
    val state = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            stickyHeader {
                /** 타이틀 영역 **/
                MainTitle(titleText = "로그인", isExpand = state.isScrolled.not())
            }
            item {
                /** 로그인 안내 영역 **/
                LoginGuideArea()
                /** 로그인 영역 **/
                LoginArea(modifier = Modifier.wrapContentHeight())
            }
        }
        /** 풋터 영역 **/
        FooterWithButton(
            text = "로그인하기",
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }
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
            text = "안녕하세요.\n스타벅스입니다.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 27.dp, start = 22.dp)
        )

        Text(
            text = "회원 서비스 이용을 위해 로그인 해주세요.",
            style = getTextStyle(Constants.Text_14),
            modifier = Modifier.padding(top = 10.dp, start = 22.dp)
        )
    }
}

/** 로그인 영역 **/
@Composable
fun LoginArea(modifier: Modifier = Modifier) {
    val id = remember {
        mutableStateOf("")
    }
    val pw = remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 80.dp)
    ) {
        CommonTextField(
            value = id.value,
            onValueChange = {
                id.value = it
            },
            hint = "아이디",
            isLabel = true
        )

        CommonTextField(
            value = pw.value,
            onValueChange = {
                pw.value = it
            },
            hint = "비밀번호",
            isLabel = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "아이디 찾기",
                style = getTextStyle(Constants.Text_12),
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 9.dp)
                    .background(BorderColor)
            )
            Text(
                text = "비밀번호 찾기",
                style = getTextStyle(Constants.Text_12),
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(1.dp, 9.dp)
                    .background(BorderColor)
            )
            Text(
                text = "회원가입",
                style = getTextStyle(Constants.Text_12),
                modifier = Modifier
            )
        }
    }
}
