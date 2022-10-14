package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.koreanCheck
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.FooterWithButton

@Composable
fun NicknameContainer(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val nickname = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.padding(horizontal = 27.dp)) {
        CheckedTextField(
            value = nickname.value,
            onValueChange = {
                if (it.length <= 6) {
                    nickname.value = it
                }
            },
            hint = "닉네임 (한글 6자리 이내)",
            isChecked = isChecked.value,
            isPassword = false,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "• 매장에서 주문한 메뉴를 찾으실 때, 등록한 닉네임으로 불러드리비다.",
            style = Typography.caption,
            color = DarkGray,
            modifier = Modifier.padding(top = 17.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "건너뛰기",
            style = Typography.body2,
            color = MainColor,
            textAlign = TextAlign.End,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .nonRippleClickable { viewModel.event(SignUpEvent.Complete) }
        )
    }

    /** 풋터 영역 **/
    FooterWithButton(
        isEnabled = nickname.value.isNotEmpty() && nickname.value.length <= 6,
        text = "다음"
    ) {
        if (koreanCheck(nickname.value)) {
            viewModel.event(SignUpEvent.NicknameResult(nickname = nickname.value))
        } else {
            context.toast("한글로만 6자입력해주세요.")
        }
    }
}