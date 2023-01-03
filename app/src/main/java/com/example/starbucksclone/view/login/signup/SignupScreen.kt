package com.example.starbucksclone.view.login.signup

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.*
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.CustomCheckBox
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.dialog.CommonTitleDialog
import com.example.starbucksclone.view.navigation.RouteAction

@Composable
fun SignupScreen(
    routeAction: RouteAction,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val status = viewModel.status.collectAsState().value
    val step = viewModel.step.value
    val context = LocalContext.current
    val isShow = remember {
        mutableStateOf(false)
    }
    val errorMessage = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        /** 헤더 영역 **/
        SignupHeader(
            step = step,
            viewModel = viewModel,
            routeAction = routeAction
        )
        /** 바디 영역 **/
        SignupBody(
            step = step,
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
        /** 풋터 영역 : 키보드 Closed 상태에서만 보임 **/
        SignupFooter(
            step = step,
            viewModel = viewModel,
            context = context
        )
    }

    when (status) {
        is SignupViewModel.SignupStatus.Init -> {}
        is SignupViewModel.SignupStatus.CertificationNumber -> {
            context.toast(status.number)
        }
        is SignupViewModel.SignupStatus.Error -> {
            isShow.value = true
            errorMessage.value = status.message
        }
        is SignupViewModel.SignupStatus.SignupComplete -> {
            routeAction.goToScreen(RouteAction.SignupComplete, true)
        }
    }

    CommonTitleDialog(
        title = errorMessage.value,
        isShow = isShow.value,
        okText = stringResource(id = R.string.ok),
        okClickListener = {
            isShow.value = false
        },
        isCancelVisible = false
    )
}

/** 해더 영역 **/
@Composable
fun SignupHeader(
    routeAction: RouteAction,
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
                    .nonRippleClickable { routeAction.popupBackStack() }
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
                SignupHeaderMessage(stringResource(id = R.string.input_id_password))
            }
            3 -> {
                SignupHeaderMessage(stringResource(id = R.string.input_email))
            }
            4 -> {
                SignupHeaderMessage(stringResource(id = R.string.input_nickname))
            }
        }
    }
}

/** 진행단계 표시 **/
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

/** 본인 인증 해더 **/
@Composable
fun IdentificationHeader(
    isAgree: Boolean,
    checkedChangeListener: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.identification),
            style = getTextStyle(22),
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 27.dp)
        )

        CustomCheckBox(
            text = stringResource(id = R.string.identification_all_agree),
            selected = isAgree,
            onClick = {
                checkedChangeListener(isAgree.not())
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (isAgree.not()) {
            IdentificationItem(stringResource(id = R.string.phone_identification_agree))
            IdentificationItem(stringResource(id = R.string.news_agency_agree))
            IdentificationItem(stringResource(id = R.string.privacy_agree))
            IdentificationItem(stringResource(id = R.string.unique_identification))
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

/**
 * 본인 인증 약관
 * 상세 페이지는 지원 안함
 * **/
@Composable
fun IdentificationItem(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 27.dp, end = 23.dp, top = 1.dp, bottom = 1.dp)
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

/** 본인 인증 외 해더 메시지 **/
@Composable
fun SignupHeaderMessage(text: String) {
    Text(
        text = text,
        style = getTextStyle(22),
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 27.dp)
    )
}

/** 바디 영역 **/
@Composable
fun SignupBody(
    step: Int,
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    when (step) {
        /** 본인 인증 **/
        1 -> {
            IdentificationBody(viewModel = viewModel, modifier = modifier)
        }
        /** 아이디와 비밀번호 **/
        2 -> {
            IdPasswordBody(viewModel = viewModel, modifier = modifier)
        }
        /** 이메일 **/
        3 -> {
            EmailBody(viewModel = viewModel, modifier = modifier)
        }
        /** 닉네임 **/
        4 -> {
            NicknameBody(viewModel = viewModel, modifier = modifier)
        }
    }
}

/** 본인인증 **/
@Composable
fun IdentificationBody(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    val certificationNumber = viewModel.certificationNumber.value

    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            CommonTextField(
                value = viewModel.signupInfo.value.name,
                onValueChange = {
                    viewModel.event(
                        SignupEvent.TextChange(type = SignupViewModel.Name, text = it)
                    )
                },
                hint = stringResource(id = R.string.name),
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp)
            )
            CommonTextField(
                value = viewModel.signupInfo.value.birthday,
                onValueChange = {
                    if (specialCharacterRestrictions(it) && it.length <= 6) {
                        viewModel.event(
                            SignupEvent.TextChange(
                                type = SignupViewModel.Birthday,
                                text = it
                            )
                        )
                    }
                },
                hint = stringResource(id = R.string.date_of_birth),
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
                        if (specialCharacterRestrictions(it) && it.length <= 11) {
                            viewModel.event(
                                SignupEvent.TextChange(
                                    type = SignupViewModel.Phone,
                                    text = it
                                )
                            )
                        }
                    },
                    hint = stringResource(id = R.string.phone_number),
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier

                )
                Text(
                    text = if (certificationNumber.isEmpty()) {
                        stringResource(id = R.string.certification_request)
                    } else {
                        stringResource(id = R.string.request_retry)
                    },
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
                        value = viewModel.userCertificationNumber.value,
                        onValueChange = {
                            if (specialCharacterRestrictions(it) && it.length <= 6) {
                                viewModel.event(
                                    SignupEvent.TextChange(
                                        type = SignupViewModel.CertificationNumber,
                                        text = it
                                    )
                                )
                            }
                        },
                        hint = stringResource(id = R.string.certification_number),
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier

                    )
                    Text(
                        text = viewModel.certificationTime.value,
                        style = getTextStyle(size = 14),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(bottom = 18.dp)
                    )
                }
            }
        }
    }
}

/** 아이디와 비밀번호 **/
@Composable
fun IdPasswordBody(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    val passwordVisible = remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }
    val passwordCheckVisible = remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 27.dp),
        modifier = modifier
    ) {
        item {
            CommonTextField(
                value = viewModel.signupInfo.value.id,
                onValueChange = {
                    if (it.length <= 13) {
                        viewModel.event(
                            SignupEvent.TextChange(
                                text = it,
                                type = SignupViewModel.Id
                            )
                        )
                    }
                },
                hint = stringResource(id = R.string.id_guide),
                imeAction = ImeAction.Next,
                trailingIcons = {
                    if (viewModel.signupInfo.value.id.length in 4..13) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null
                        )
                    }
                }
            )
            CommonTextField(
                value = viewModel.signupInfo.value.password,
                onValueChange = {
                    if (it.length <= 20) {
                        viewModel.event(
                            SignupEvent.TextChange(
                                text = it,
                                type = SignupViewModel.Password
                            )
                        )
                    }
                },
                visualTransformation = passwordVisible.value,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                hint = stringResource(id = R.string.password_guide),
                trailingIcons = {
                    Row {
                        Image(
                            painter = painterResource(id = if (passwordVisible.value == PasswordVisualTransformation()) R.drawable.ic_unvisibility else R.drawable.ic_visibility),
                            contentDescription = null,
                            modifier = Modifier.nonRippleClickable {
                                passwordVisible.value =
                                    if (passwordVisible.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                            }
                        )
                        if (viewModel.signupInfo.value.password.length in 10..20) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 14.dp)
                            )
                        }
                    }
                }
            )
            CommonTextField(
                value = viewModel.passwordCheck.value,
                onValueChange = {
                    if (it.length <= 13) {
                        viewModel.event(
                            SignupEvent.TextChange(
                                text = it,
                                type = SignupViewModel.PasswordCheck
                            )
                        )
                    }
                },
                visualTransformation = passwordCheckVisible.value,
                keyboardType = KeyboardType.Password,
                hint = stringResource(id = R.string.password_check),
                trailingIcons = {
                    Row {
                        Image(
                            painter = painterResource(id = if (passwordCheckVisible.value == PasswordVisualTransformation()) R.drawable.ic_unvisibility else R.drawable.ic_visibility),
                            contentDescription = null,
                            modifier = Modifier.nonRippleClickable {
                                passwordCheckVisible.value =
                                    if (passwordCheckVisible.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                            }
                        )
                        if (viewModel.signupInfo.value.password == viewModel.passwordCheck.value) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 14.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}

/** 이메일 **/
@Composable
fun EmailBody(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 27.dp),
        modifier = modifier
    ) {
        item {
            CommonTextField(
                value = viewModel.signupInfo.value.email,
                onValueChange = {
                    viewModel.event(SignupEvent.TextChange(text = it, type = SignupViewModel.Email))
                },
                hint = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.email_description1),
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.email_description2),
                style = getTextStyle(size = 12, color = DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        }
    }
}

/** 닉네임 **/
@Composable
fun NicknameBody(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 27.dp),
        modifier = modifier
    ) {
        item {
            CommonTextField(
                value = viewModel.signupInfo.value.nickname,
                onValueChange = {
                    if (it.length <= 6) {
                        viewModel.event(
                            SignupEvent.TextChange(
                                text = it,
                                type = SignupViewModel.Nickname
                            )
                        )
                    }
                },
                hint = stringResource(id = R.string.nickname_guide)
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.nickname_description),
                style = getTextStyle(size = 12, color = DarkGray)
            )
        }
    }
}

/** 풋터 영역 **/
@Composable
fun SignupFooter(
    step: Int,
    viewModel: SignupViewModel,
    context: Context
) {
    val isKeyboardOpen by keyboardAsState()
    if (isKeyboardOpen == Keyboard.Closed) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (step == 4) {
                Text(
                    text = stringResource(id = R.string.skip),
                    style = getTextStyle(18, false, MainColor),
                    modifier = Modifier
                        .padding(end = 27.dp, bottom = 20.dp)
                        .align(Alignment.End)
                        .nonRippleClickable {
                            viewModel.event(SignupEvent.Signup)
                        }
                )
            }
            FooterWithButton(
                text = stringResource(id = R.string.next),
                isEnabled = viewModel.isEnable.value
            ) {
                when (step) {
                    1 -> {
                        if (viewModel.userCertificationNumber.value == viewModel.certificationNumber.value) {
                            viewModel.event(SignupEvent.NextStep)
                        } else {
                            context.toast(R.string.certification_number_check)
                        }
                    }
                    4 -> {
                        viewModel.event(SignupEvent.Signup)
                    }
                    else -> {
                        viewModel.event(SignupEvent.NextStep)
                    }
                }
            }
        }
    }
}