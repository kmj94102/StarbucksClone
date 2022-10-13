package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.UserTemp
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.specialCharacterRestrictions
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonRadioButton
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton

@Composable
fun SelfAuthenticationContainer(
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier
) {
    val selected = remember { mutableStateOf(false) }
    val userInfo = remember { mutableStateOf(UserTemp()) }
    val certificationNumber = remember { mutableStateOf("") }
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),
        modifier = modifier
    ) {
        // 본인 인증 서비스 약과 동의
        item {
            CommonRadioButton(
                text = "본인 인증 서비스 약관 전체동의",
                selected = selected.value,
                onClick = { selected.value = selected.value.not() },
                modifier = Modifier.padding(start = 22.dp)
            )

            if (selected.value.not()) {
                TermsItem(
                    text = "휴대폰 본인 인증 서비스 이용약관 동의 (필수)",
                    modifier = Modifier.padding(top = 0.dp)
                ) {
                    context.toast("준비중입니다.")
                }
                TermsItem(
                    text = "휴대폰 통신사 이용약관 (필수)",
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    context.toast("준비중입니다.")
                }
                TermsItem(
                    text = "개인정보 제공 및 이용 동의 (필수)",
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    context.toast("준비중입니다.")
                }
                TermsItem(
                    text = "고유식별정보 처리 동의 (필수)",
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    context.toast("준비중입니다.")
                }
            }

            Divider(color = Gray, modifier = Modifier.padding(top = 21.dp))
        }// 본인 인증 서비스 약과 동의

        item {
            SelfAuthentication(userInfo, certificationNumber, viewModel)
        }
    }

    /** 풋터 영역 **/
    FooterWithButton(
        isEnabled = nextStateCheck(
            selected = selected.value,
            userInfo = userInfo.value
        ),
        text = "다음"
    ) {
        if (viewModel.certificationNumber.value != certificationNumber.value) {
            context.toast("인증번호가 일치하지 않습니다.")
        } else {
            viewModel.event(
                SignUpEvent.SelfAuthenticationResult(
                    name = userInfo.value.name,
                    birthday = userInfo.value.birthday,
                    phone = userInfo.value.phone
                )
            )
        }
    }
}

@Composable
fun TermsItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 27.dp, end = 23.dp)
            .nonRippleClickable {
                onClick()
            }
    ) {
        Text(
            text = text,
            style = Typography.caption,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = "next",
            tint = DarkGray
        )
    }
}

@Composable
fun SelfAuthentication(
    userInfo: MutableState<UserTemp>,
    certificationNumber: MutableState<String>,
    viewModel: SignUpViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.padding(top = 15.dp)
    ) {
        // 이름
        CommonTextField(
            value = userInfo.value.name,
            imeAction = ImeAction.Next,
            onValueChange = { userInfo.value = userInfo.value.copy(name = it) },
            hint = "이름",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
        )
        CommonTextField(
            value = userInfo.value.birthday,
            onValueChange = {
                if (it.length <= 6 && specialCharacterRestrictions(it)) {
                    userInfo.value = userInfo.value.copy(birthday = it)
                }
            },
            imeAction = ImeAction.Next,
            hint = "생년월일 6자리",
            keyboardType = KeyboardType.Number,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
        )

        PhoneTextField(
            value = userInfo.value.phone,
            onChange = {
                if (it.length <= 11 && specialCharacterRestrictions(it)) {
                    userInfo.value = userInfo.value.copy(phone = it)
                }
            },
            viewModel = viewModel
        )

        if (viewModel.certificationNumber.value.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                CommonTextField(
                    value = certificationNumber.value,
                    onValueChange = { certificationNumber.value = it },
                    keyboardType = KeyboardType.Number,
                    hint = "인증번호 6자리",
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = viewModel.certificationTime.value,
                    style = Typography.body1,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        Text(
            text = "• 타인의 개인정보를 도용하여 가입한 경우, 서비스 이용 제한 및 법적 제재를 받으실 수 있습니다.",
            style = Typography.caption,
            color = Black,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)
        )
    }
}

@Composable
fun PhoneTextField(
    value: String,
    onChange: (String) -> Unit,
    viewModel: SignUpViewModel
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        val startGuideline = createGuidelineFromStart(0.3f)
        val context = LocalContext.current
        val (phoneCarrier, phone, certification, divider) = createRefs()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(.1f)
                .constrainAs(phoneCarrier) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(startGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            Text(text = "SKT", style = Typography.body2, modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "down"
            )
        }

        CommonTextField(
            value = value,
            onValueChange = onChange,
            hint = "휴대폰 번호",
            keyboardType = KeyboardType.Number,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth(.2f)
                .padding(start = 5.dp)
                .constrainAs(phone) {
                    top.linkTo(parent.top)
                    start.linkTo(phoneCarrier.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        if (viewModel.certificationNumber.value.isEmpty()) {
            Text(
                text = "인증요청",
                style = Typography.body1,
                fontSize = 14.sp,
                color = if (value.length in 10..11) MainColor else DarkGray,
                modifier = Modifier
                    .nonRippleClickable {
                        if (value.length in 10..11) {
                            viewModel.event(
                                SignUpEvent.NewCertificationNumber {
                                    context.toast("인증번호 : $it")
                                }
                            )
                        }
                    }
                    .constrainAs(certification) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        } else {
            Text(
                text = "다시요청",
                style = Typography.body1,
                fontSize = 14.sp,
                color = MainColor,
                modifier = Modifier
                    .nonRippleClickable {
                        if (value.length in 10..11) {
                            viewModel.event(
                                SignUpEvent.NewCertificationNumber {
                                    context.toast("인증번호 : $it")
                                }
                            )
                        }
                    }
                    .constrainAs(certification) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        Box(
            modifier = Modifier
                .height(1.dp)
                .background(Gray)
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })
    }
}

fun nextStateCheck(
    selected: Boolean,
    userInfo: UserTemp,
): Boolean {
    if (userInfo.name.length < 2) return false
    if (userInfo.phone.length !in 10..11) return false
    if (userInfo.birthday.length != 6) return false
    return selected
}