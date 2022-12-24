package com.example.starbucksclone.view.main.order.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.LightGray
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.view.common.CommonTextField
import com.example.starbucksclone.view.dialog.CommonTitleDialog

@Composable
fun MyMenuRegisterDialog(
    isShow: Boolean,
    name: String,
    property: String,
    okClickListener: (String) -> Unit,
    cancelClickListener: () -> Unit
) {
    val anotherName = remember {
        mutableStateOf("")
    }
    CommonTitleDialog(
        title = "나만의 메뉴로 등록해보세요.",
        isShow = isShow,
        contentPadding = 20.dp,
        contents = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightGray)
                ) {
                    Text(
                        text = name,
                        style = getTextStyle(16, true),
                        modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)
                    )
                    Text(
                        text = property,
                        style = getTextStyle(14, false, DarkGray),
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 8.dp,
                            start = 12.dp,
                            end = 12.dp
                        )
                    )
                }

                Text(
                    text = "등록할 나만의 메뉴 이름을 지어보세요",
                    style = getTextStyle(12),
                    modifier = Modifier.padding(top = 8.dp)
                )
                CommonTextField(
                    value = anotherName.value,
                    onValueChange = {
                        anotherName.value = it
                    },
                    hint = "나만의 $name",
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                )
            }
        },
        okText = "확인",
        okClickListener = {
            okClickListener(anotherName.value)
        },
        cancelText = "취소",
        cancelClickListener = {
            cancelClickListener()
        }
    )
}