package com.example.starbucksclone.view.login.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.view.common.Title
import com.example.starbucksclone.view.navigation.RoutAction
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.Gray
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonRadioButton
import com.example.starbucksclone.view.common.FooterWithButton

@Composable
fun TermsScreen(
    routAction: RoutAction,
    viewModel: TermsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        /** 타이틀 영역 **/
        Title(
            leftIconRes = R.drawable.ic_close,
            onLeftIconClick = { routAction.popupBackStack() }
        )
        /** 바디 영역 **/
        TermsBody(viewModel = viewModel, modifier = Modifier.weight(1f))
        /** 풋터 영역 **/
        FooterWithButton(text = "다음", isEnabled = viewModel.termsOfServiceState.value && viewModel.privacyState.value) {
            routAction.goToSignUp(viewModel.pushState.value)
        }
    }
}

@Composable
fun TermsBody(viewModel: TermsViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(82.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "고객님\n환영합니다!",
                lineHeight = 28.sp,
                style = Typography.subtitle1,
                fontWeight = FontWeight.Normal
            )
        }
        /** 약관 동의 **/
        TermsItem(viewModel)
    }
}

/** 약관 동의 **/
@Composable
fun TermsItem(viewModel: TermsViewModel) {
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 23.dp)
    ) {

        val (terms, privacy, push, all, divider, guide) = createRefs()

        CommonRadioButton(
            text = "약관 전체 동의",
            selected = viewModel.termsOfServiceState.value && viewModel.privacyState.value && viewModel.pushState.value,
            onClick = {
                viewModel.event(TermsEvent.AllChange)
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(all) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        CommonRadioButton(
            text = "이용약관 동의(필수)",
            selected = viewModel.termsOfServiceState.value,
            onClick = { viewModel.event(TermsEvent.TermsOfServiceChange) },
            isNextButton = true,
            onNextClick = { context.toast("준비중입니다.") },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(terms) {
                    top.linkTo(all.bottom, 10.dp)
                    start.linkTo(all.start)
                }
        )

        CommonRadioButton(
            text = "개인정보 수집 및 이동동의(필수)",
            selected = viewModel.privacyState.value,
            onClick = { viewModel.event(TermsEvent.PrivacyChange) },
            isNextButton = true,
            onNextClick = { context.toast("준비중입니다.") },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(privacy) {
                    top.linkTo(terms.bottom)
                    start.linkTo(all.start)
                }
        )

        CommonRadioButton(
            text = "E-mail 및 SMS 광고성 정보 수진동의(선택)",
            selected = viewModel.pushState.value,
            onClick = { viewModel.event(TermsEvent.PushChange) },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(push) {
                    top.linkTo(privacy.bottom)
                    start.linkTo(all.start)
                }
        )

        Divider(
            color = Gray,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(all.bottom)
                bottom.linkTo(terms.top)
            }
        )

        Text(
            text = "다양한 프로모션 소식 및 신규 매장 정보를 보내 드립니다.",
            style = Typography.caption,
            color = DarkGray,
            modifier = Modifier.constrainAs(guide) {
                top.linkTo(push.bottom, (-10).dp)
                start.linkTo(push.start, 47.dp)
                bottom.linkTo(parent.bottom, 35.dp)
            }
        )

    }

}