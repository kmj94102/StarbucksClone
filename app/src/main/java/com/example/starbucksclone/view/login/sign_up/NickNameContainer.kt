package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.nonRippleClickable

@Composable
fun NicknameContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val temp = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(horizontal = 27.dp)) {
        CheckedTextField(
            value = temp.value,
            onValueChange = { temp.value = it },
            hint = "닉네임 (한글 6자리 이내)",
            isChecked = isChecked.value,
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
                .nonRippleClickable { onClick() }
        )
    }
}