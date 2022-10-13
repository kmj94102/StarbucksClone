package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.starbucksclone.R
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toast
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.common.FooterWithButton

@Composable
fun IdPasswordSettingContainer(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val id = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordCheck = remember { mutableStateOf("") }
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp)
    ) {
        item {
            CheckedTextField(
                value = id.value,
                onValueChange = {
                    if (it.length <= 13) {
                        id.value = it
                    }
                },
                hint = "아이디 (4~13자리 이내)",
                isChecked = id.value.length in 4..13,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                isPassword = false,
                modifier = modifier.fillMaxWidth()
            )
        }
        item {
            CheckedTextField(
                value = password.value,
                onValueChange = {
                    if (it.length <= 20) {
                        password.value = it
                    }
                },
                hint = "비밀번호 (10~20자리 이내)",
                isChecked = password.value.length in 10..20,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                modifier = modifier.fillMaxWidth()
            )
        }

        item {
            CheckedTextField(
                value = passwordCheck.value,
                onValueChange = {
                    if (it.length <= 20) {
                        passwordCheck.value = it
                    }
                },
                hint = "비밀번호 확인",
                isChecked = passwordCheck.value.length in 10..20 && passwordCheck.value == password.value,
                keyboardType = KeyboardType.Password,
                modifier = modifier.fillMaxWidth()
            )
        }
    }

    /** 풋터 영역 **/
    FooterWithButton(
        isEnabled = nextCheck(
            id = id.value,
            password = password.value,
            passwordCheck = passwordCheck.value
        ),
        text = "다음"
    ) {
        if (password.value != passwordCheck.value) {
            context.toast("비밀번호를 확인해주세요")
        } else {
            viewModel.event(
                SignUpEvent.IdPasswordResult(
                    id = id.value,
                    password = password.value
                )
            )
        }
    }
}

@Composable
fun CheckedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions(),
    isChecked: Boolean = false,
    isPassword: Boolean = true,
    modifier: Modifier
) {
    val isVisible = remember { mutableStateOf(isPassword.not()) }

    Box(modifier = modifier) {
        CommonTextField(
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            visualTransformation =
            if (isVisible.value.not()) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth()
        )

        if (isChecked) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "check",
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .padding(end = 4.dp)
            )
        }

        if (keyboardType == KeyboardType.Password) {
            Image(
                painter = painterResource(id = if (isVisible.value) R.drawable.ic_visibility else R.drawable.ic_unvisibility),
                contentDescription = "visibility",
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .padding(end = 32.dp)
                    .nonRippleClickable {
                        isVisible.value = isVisible.value.not()
                    }
            )
        }

    }
}

private fun nextCheck(
    id: String,
    password: String,
    passwordCheck: String
): Boolean {
    if (id.length !in 4..13) return false
    if (password.length !in 10..20) return false
    if (passwordCheck.length !in 10..20) return false

    return true
}