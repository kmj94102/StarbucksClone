package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton

@Composable
fun EmailSettingContainer(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val email = remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp)
    ) {

        item {
            CommonTextField(
                value = email.value,
                onValueChange = { email.value = it },
                hint = "이메일을 입력해주세요",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(17.dp))
            Text(text = "• 스타벅스커피 코리아의 새로운 서비스와 최신 이벤트 정보를 이메일로 보내드려요.")
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "• 주요 공지사항 및 이벤트 당첨안내 등 일부 소식은 수신동의 여부에 관계없이 발송됩니다.")
        }

    }

    /** 풋터 영역 **/
    FooterWithButton(
        isEnabled = email.value.length > 10,
        text = "다음"
    ) {
        viewModel.event(SignUpEvent.EmailResult(email = email.value))
    }
}