package com.example.starbucksclone.view.login.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.CustomCheckBox
import com.example.starbucksclone.view.common.FooterWithButton
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun TermsScreen(routAction: RoutAction) {
    val termsOfService = remember {
        mutableStateOf(false)
    }
    val collectionConsent = remember {
        mutableStateOf(false)
    }
    val pushConsent = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        /** x 버튼 **/
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close",
            modifier = Modifier
                .padding(top = 9.dp, start = 16.dp)
                .nonRippleClickable {
                    routAction.popupBackStack()
                }
        )
        /** 환영 메시지 **/
        WelcomeMessage(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 27.dp)
        )
        /** 약관 동의 영역 **/
        TermsArea(termsOfService, collectionConsent, pushConsent)
        /** 풋터 : 다음 버튼 **/
        FooterWithButton(
            text = "다음",
            isEnabled = termsOfService.value && collectionConsent.value
        ) {
            routAction.goToSignup(pushConsent.value)
        }
    }
}

/** 환영 메시지 **/
@Composable
fun WelcomeMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "logo",
            modifier = Modifier
                .padding(top = 90.dp)
                .size(66.dp)
        )
        Text(
            text = "고객님\n환영합니다!",
            style = getTextStyle(22),
            modifier = Modifier.padding(top = 40.dp, bottom = 100.dp)
        )
    }
}

/** 약관 동의 **/
@Composable
fun TermsArea(
    termsOfService: MutableState<Boolean>,
    collectionConsent: MutableState<Boolean>,
    pushConsent: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomCheckBox(
            text = "약관 전체 동의",
            textStyle = getTextStyle(14),
            selected = termsOfService.value && collectionConsent.value && pushConsent.value,
            onClick = {
                val value =
                    (termsOfService.value && collectionConsent.value && pushConsent.value).not()
                termsOfService.value = value
                collectionConsent.value = value
                pushConsent.value = value
            },
            modifier = Modifier.padding(start = 24.dp)
        )
        Box(
            modifier = Modifier
                .background(Gray)
                .fillMaxWidth()
                .height(1.dp)
                .padding(top = 7.dp)
        )

        CustomCheckBox(
            text = "이용약관 동의(필수)",
            textStyle = getTextStyle(14),
            selected = termsOfService.value,
            onClick = { termsOfService.value = termsOfService.value.not() },
            modifier = Modifier.padding(start = 24.dp, top = 3.dp)
        )
        CustomCheckBox(
            text = "개인정보 수집 및 이용동의(필수)",
            textStyle = getTextStyle(14),
            selected = collectionConsent.value,
            onClick = { collectionConsent.value = collectionConsent.value.not() },
            modifier = Modifier.padding(start = 24.dp, top = 14.dp)
        )
        CustomCheckBox(
            text = "E-mail 및 SMS 광고성 정보 수신동의(선택)",
            textStyle = getTextStyle(14),
            selected = pushConsent.value,
            onClick = { pushConsent.value = pushConsent.value.not() },
            modifier = Modifier.padding(start = 24.dp, top = 14.dp)
        )
        Text(
            text = "다양한 프로모션 소식 및 신규 매장 정보를 보내드립니다.",
            style = getTextStyle(size = 12, color = DarkGray),
            modifier = Modifier.padding(start = 55.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
    }
}

