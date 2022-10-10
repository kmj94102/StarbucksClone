package com.example.starbucksclone.view.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable

/**
 * 상단 타이틀
 * @param leftIconRes 좌측 아이콘 이미지 res
 * @param onLeftIconClick 좌측 아이콘 클릭 리스너
 * @param title 상단 타이틀
 * @param lazyListSate 스크롤 상태
 * @param modifier Modifier
 * **/
@Composable
fun Title(
    @DrawableRes leftIconRes: Int = R.drawable.ic_back,
    onLeftIconClick: () -> Unit,
    title: String = "",
    lazyListSate: LazyListState? = null,
    modifier: Modifier = Modifier
) {

    val elevation = if (lazyListSate?.isScrolled == true) 6.dp else 0.dp

    Surface(shadowElevation = elevation) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(White)
                .height(41.dp)
        ) {
            Icon(
                painter = painterResource(id = leftIconRes),
                contentDescription = "titleIcon",
                modifier = Modifier
                    .nonRippleClickable { onLeftIconClick() }
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )

            if (lazyListSate?.isScrolled == true) {
                Text(
                    text = title,
                    style = Typography.body1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * 라운드 버튼
 * @param text [필수] 버튼 문구
 * @param round 버튼 라운드 값
 * @param isEnabled 활성화 여부
 * @param isOutline 아웃라인 형식 여부
 * @param buttonColor 버튼 색상
 * @param modifier Modifier
 * @param onClick 버튼 클릭 리스너
 * **/
@Composable
fun RoundedButton(
    text: String,
    round: Dp = 20.dp,
    isEnabled: Boolean = true,
    isOutline: Boolean = false,
    buttonColor: Color = MainColor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = buttonColor,
            contentColor = White,
            disabledContainerColor = Gray,
            disabledContentColor = White
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(round),
        border = BorderStroke(1.dp, if (isOutline) MainColor else Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = Typography.body1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

/**
 * 하단 버튼
 * @param text [필수] 버튼 문구
 * @param round 버튼 라운드 값
 * @param isEnabled 버튼 활성화 여부
 * @param buttonColor 버튼 색상
 * @param buttonModifier 버튼 Modifier
 * **/
@Composable
fun FooterWithButton(
    text: String,
    round: Dp = 20.dp,
    isEnabled: Boolean = true,
    buttonColor: Color = MainColor,
    buttonModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(shadowElevation = 15.dp, color = White) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        ) {
            Divider(color = Gray, modifier = Modifier.align(Alignment.TopCenter))
            RoundedButton(
                text = text,
                isEnabled = isEnabled,
                round = round,
                buttonColor = buttonColor,
                modifier = buttonModifier
                    .padding(vertical = 16.dp, horizontal = 23.dp),
            ) {
                onClick()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions(),
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = White,
            cursorColor = MainColor,
            focusedIndicatorColor = MainColor,
            unfocusedIndicatorColor = Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        textStyle = Typography.body1,
        placeholder = {
            Text(text = hint, style = Typography.body1)
        },
        modifier = modifier
    )
}

@Composable
fun CommonRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    isNextButton: Boolean = false,
    onNextClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.nonRippleClickable {
            onClick()
        }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MainColor,
                unselectedColor = Gray
            )
        )
        Text(
            text = text,
            style = Typography.body1,
            modifier = Modifier
                .weight(1f)
        )
        if (isNextButton) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = "next",
                tint = DarkGray,
                modifier = Modifier.nonRippleClickable {
                    onNextClick?.invoke()
                }
            )
        }
    }
}