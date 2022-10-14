package com.example.starbucksclone.view.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * @param textColor 텍스트 색상
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
    textColor: Color = White,
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
            containerColor = if (isOutline) Color.Transparent else buttonColor,
            contentColor = textColor,
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
    isLabel: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusedIndicatorColor: Color = MainColor,
    unfocusedIndicatorColor: Color = Gray,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions(),
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }
    val color = if (isFocused) focusedIndicatorColor else unfocusedIndicatorColor

    BasicTextField(
        value = value,
        modifier = modifier
            .indicatorLine(
                enabled,
                false,
                interactionSource,
                TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = color
                )
            )
            .focusRequester(focusRequester = focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        readOnly = false,
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        enabled = enabled,
        keyboardActions = keyboardActions,
        textStyle = Typography.body2,
        cursorBrush = SolidColor(MainColor),
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                placeholder = if (isLabel.not()) {
                    {
                        Text(
                            text = hint,
                            style = Typography.body1,
                            fontSize = 14.sp,
                            color = DarkGray
                        )
                    }
                } else {
                    null
                },
                label = if (isLabel) {
                    {
                        Text(text = hint, style = Typography.body1, fontSize = 14.sp, color = Black)
                    }
                } else {
                    null
                }
            )
        }
    )
}

/**
 * 커스텀 체크 박스
 * @param text 체크박스 문구
 * @param selected 체크박스 선택 여부
 * @param onClick 체크박스 클릭 리스너
 * @param isNextButton 체크박스 오른쪽에 더보기 버튼 여부
 * @param onNextClick 더보기 버튼 클릭 리스너
 * @param modifier Modifier
 * **/
@Composable
fun CommonCheckBox(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    isNextButton: Boolean = false,
    onNextClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 10.dp)
            .nonRippleClickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = if (selected) R.drawable.ic_radio_check else R.drawable.ic_radio_unchecked),
            contentDescription = "checkbox"
        )
        Spacer(modifier = Modifier.width(8.dp))
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