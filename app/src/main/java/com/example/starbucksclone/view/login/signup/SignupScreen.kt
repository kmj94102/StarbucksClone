package com.example.starbucksclone.view.login.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.CustomCheckBox
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun SignupScreen(
    routAction: RoutAction,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val step = viewModel.step.value

    Column(modifier = Modifier.fillMaxSize()) {
        SignupHeader(
            step = step,
            viewModel = viewModel,
            routAction = routAction
        )
        SignupBody(
            step = step,
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
        SignupFooter(
            step = step,
            routAction = routAction,
            viewModel = viewModel
        )
    }
}

@Composable
fun StepProgress(
    step: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        (1..4).forEach {
            val isCurrentStep = it == step
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(16.dp)
                    .background(if (isCurrentStep) Black else White)
                    .border(width = 1.dp, color = if (isCurrentStep) Black else Gray, CircleShape)
            ) {
                Text(
                    text = "$it",
                    style = getTextStyle(
                        size = 10,
                        isBold = true,
                        color = if (isCurrentStep) White else Gray
                    )
                )
            }
            if (it != 4) {
                Box(
                    modifier = Modifier
                        .background(Gray)
                        .size(10.dp, 1.dp)
                )
            }
        }
    }
}

@Composable
fun SignupHeader(
    routAction: RoutAction,
    viewModel: SignupViewModel,
    step: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "close",
                modifier = Modifier
                    .padding(top = 9.dp, start = 16.dp)
                    .nonRippleClickable { routAction.popupBackStack() }
            )
            StepProgress(
                step = step,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 13.dp)
            )
        }
        when (step) {
            1 -> {
                IdentificationHeader(viewModel.isIdentificationAgree.value) {
                    viewModel.event(SignupEvent.IdentificationAgree(it))
                }
            }
            2 -> {
                SignupHeaderMessage("아이디와 비밀번호를\n입력해주세요.")
            }
            3 -> {
                SignupHeaderMessage("이메일을\n입력해주세요.")
            }
            4 -> {
                SignupHeaderMessage("닉네임을\n입력해주세요.")
            }
        }
    }
}

@Composable
fun IdentificationHeader(
    isAgree: Boolean,
    checkedChangeListener: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "본인확인을 위해\n인증을 진행해 주세요.",
            style = getTextStyle(22),
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 27.dp)
        )

        CustomCheckBox(
            text = "본인 인증 서비스 야갸관 전체 동의",
            selected = isAgree,
            onClick = {
                checkedChangeListener(isAgree.not())
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (isAgree.not()) {
            IdentificationItem("휴대폰 본인 인증 서비스 이용약관 동의 (필수)")
            IdentificationItem("휴대폰 통신사 이용약관 동의 (필수)")
            IdentificationItem("개인정보 제공 및 이용 동의 (필수)")
            IdentificationItem("고유식별정보 처리 (필수)")
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
fun IdentificationItem(
    text: String
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 27.dp, end = 23.dp, top = 1.dp, bottom = 1.dp)
            .nonRippleClickable {
                context.toast("클론 프로젝트에선 제공하지 않는 기능입니다.")
            }
    ) {
        Text(
            text = text,
            style = getTextStyle(size = 12, color = DarkGray),
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = Gray
        )
    }
}

@Composable
fun SignupHeaderMessage(text: String) {
    Text(
        text = text,
        style = getTextStyle(22),
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 27.dp)
    )
}

@Composable
fun SignupBody(
    step: Int,
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    when (step) {
        1 -> {
            IdentificationBody(viewModel = viewModel, modifier = modifier)
        }
        2 -> {

        }
        3 -> {

        }
        4 -> {

        }
    }
}

@Composable
fun IdentificationBody(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    val certificationNumber = viewModel.certificationNumber.value

    LazyColumn(modifier = modifier) {
        item {
            CommonTextField(
                value = viewModel.signupInfo.value.name,
                onValueChange = {
                    viewModel.event(SignupEvent.TextChange(type = SignupViewModel.Name, text = it))
                },
                hint = "이름",
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp)
            )
            CommonTextField(
                value = viewModel.signupInfo.value.birthday,
                onValueChange = {
                    viewModel.event(
                        SignupEvent.TextChange(
                            type = SignupViewModel.Birthday,
                            text = it
                        )
                    )
                },
                hint = "생년월일 6자리",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp)
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp)
            ) {
                CommonTextField(
                    value = viewModel.signupInfo.value.phone,
                    onValueChange = {
                        viewModel.event(
                            SignupEvent.TextChange(
                                type = SignupViewModel.Phone,
                                text = it
                            )
                        )
                    },
                    hint = "휴대폰 번호",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier

                )
                Text(
                    text = if (certificationNumber.isEmpty()) "인증요청" else "다시요청",
                    style = getTextStyle(
                        size = 14,
                        color = if (certificationNumber.isEmpty()) DarkGray else MainColor
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(bottom = 18.dp)
                        .nonRippleClickable {
                            viewModel.event(SignupEvent.NewCertificationNumber)
                        }
                )
            }
            if (certificationNumber.isNotEmpty()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 27.dp)
                ) {
                    CommonTextField(
                        value = viewModel.signupInfo.value.phone,
                        onValueChange = {
                            viewModel.event(
                                SignupEvent.TextChange(
                                    type = SignupViewModel.Phone,
                                    text = it
                                )
                            )
                        },
                        hint = "인증번호",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier

                    )
                    Text(
                        text = "인증요청",
                        style = getTextStyle(size = 14, color = DarkGray),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .nonRippleClickable {

                            }
                    )
                }
            }
        }
    }
}

@Composable
fun SignupFooter(
    routAction: RoutAction,
    step: Int,
    viewModel: SignupViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (step == 4) {
            Text(
                text = "건너뛰기",
                style = getTextStyle(18, false, MainColor),
                modifier = Modifier
                    .padding(end = 27.dp, bottom = 20.dp)
                    .align(Alignment.End)
            )
        }
        FooterWithButton(
            text = "다음",
            isEnabled = viewModel.isEnable.value
        ) {
            if (step == 4) {
                routAction.popupBackStack()
            } else {
                viewModel.event(SignupEvent.NextStep)
            }
        }
    }
}