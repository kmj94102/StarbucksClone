package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.*
import com.example.starbucksclone.view.navigation.NavigationGraph
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun SignUpScreen(routAction: RoutAction) {

    val state = remember {
        mutableStateOf(0)
    }
    val titleList = listOf(
        "본인확을 위해\n인증을 진행해주세요.", "아이디와 비밀번호를\n입력해 주세요.",
        "이메일을\n입력해 주세요.", "닉네임을\n입력해 주세요."
    )
    var nextListener: () -> Unit = {}

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            /** 타이틀 영역 **/
            Title(
                leftIconRes = R.drawable.ic_close,
                onLeftIconClick = { routAction.popupBackStack() }
            )
            /** 타이틀 문구 **/
            Text(
                text = titleList[state.value],
                style = Typography.subtitle1,
                fontWeight = FontWeight.Normal,
                lineHeight = 28.sp,
                modifier = Modifier.padding(start = 24.dp, top = 22.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))
            /** 바디 영역 **/
            when (state.value) {
                0 -> {
                    SelfAuthenticationContainer(modifier = Modifier.weight(1f))
                    nextListener = {
                        state.value = 1
                    }
                }
                1 -> {
                    IdPasswordSettingContainer(modifier = Modifier.weight(1f))
                    nextListener = {
                        state.value = 2
                    }
                }
                2 -> {
                    EmailSettingContainer(modifier = Modifier.weight(1f))
                    nextListener = {
                        state.value = 3
                    }
                }
                3 -> {

                }
            }
            /** 풋터 영역 **/
            FooterWithButton(text = "다음") {
                nextListener()
            }
        }
        /** 회원가입 진행 표시 **/
        SignUpState(
            state = state.value + 1,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 13.dp)
        )
    }

}

/** 회원가입 진행 표시 **/
@Composable
fun SignUpState(state: Int, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (step1, step2, step3, step4, divider) = createRefs()

        Divider(
            color = DarkGray,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(step1.start)
                end.linkTo(step4.end)
                width = Dimension.fillToConstraints
            }
        )

        StepElement(
            text = "1",
            isCurrent = state == 1,
            modifier = Modifier.constrainAs(step1) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
        StepElement(
            text = "2",
            isCurrent = state == 2,
            modifier = Modifier.constrainAs(step2) {
                top.linkTo(parent.top)
                start.linkTo(step1.end, 10.dp)
            }
        )
        StepElement(
            text = "3",
            isCurrent = state == 3,
            modifier = Modifier.constrainAs(step3) {
                top.linkTo(parent.top)
                start.linkTo(step2.end, 10.dp)
            }
        )
        StepElement(
            text = "4",
            isCurrent = state == 4,
            modifier = Modifier.constrainAs(step4) {
                top.linkTo(parent.top)
                start.linkTo(step3.end, 10.dp)
            }
        )
    }
}

@Composable
fun StepElement(
    text: String,
    isCurrent: Boolean,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = {},
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Black,
            contentColor = White,
            disabledContainerColor = White,
            disabledContentColor = DarkGray
        ),
        enabled = isCurrent,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isCurrent) Color.Transparent else DarkGray),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}