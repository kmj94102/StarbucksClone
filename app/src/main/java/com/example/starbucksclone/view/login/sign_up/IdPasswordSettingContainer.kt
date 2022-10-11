package com.example.starbucksclone.view.login.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.CommonTextField

@Composable
fun IdPasswordSettingContainer(modifier: Modifier = Modifier) {
    val temp = remember {
        mutableStateOf("")
    }
    val temp2 = remember {
        mutableStateOf("")
    }
    val temp3 = remember {
        mutableStateOf("")
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp)
    ) {
        item {
            CheckedTextField(
                value = temp.value,
                onValueChange = { temp.value = it },
                hint = "아이디 (4~13자리 이내)",
                isChecked = temp.value.length > 4,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                modifier = modifier.fillMaxWidth()
            )
        }
        item {
            CheckedTextField(
                value = temp2.value,
                onValueChange = { temp2.value = it },
                hint = "비밀번호 (10~20자리 이내)",
                isChecked = temp2.value.length > 4,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                modifier = modifier.fillMaxWidth()
            )
        }

        item {
            CheckedTextField(
                value = temp3.value,
                onValueChange = { temp3.value = it },
                hint = "비밀번호 확인",
                isChecked = temp3.value.length > 4,
                keyboardType = KeyboardType.Password,
                modifier = modifier.fillMaxWidth()
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isChecked: Boolean = false,
    modifier: Modifier
) {
    val isVisible = remember { mutableStateOf(false) }
    var currentKeyboardType = keyboardType

    Box(modifier = modifier) {
        CommonTextField(
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            visualTransformation =
            if (keyboardType == KeyboardType.Password && isVisible.value.not()) PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardType = currentKeyboardType,
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
                        currentKeyboardType =
                            if (isVisible.value) KeyboardType.Text else KeyboardType.Password
                    }
            )
        }

    }
}