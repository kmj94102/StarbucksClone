package com.example.starbucksclone.view.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.isScrolled
import com.example.starbucksclone.util.nonRippleClickable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
 * 모션 상단 타이틀 : 스크롤 상태에 따라 움직이는 타이틀의 위치가 변하는 경우 사용
 * @param leftIconRes 좌측 아이콘 이미지 res
 * @param onLeftIconClick 좌측 아이콘 클릭 리스너
 * @param rightIconRes 우측 아이콘 이미지 res
 * @param onRightIconClick 우측 아이콘 클릭 리스너
 * @param titleText 상단 타이틀
 * @param lazyListSate 스크롤 상태
 * @param modifier Modifier
 * **/
@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionTitle(
    @DrawableRes leftIconRes: Int? = R.drawable.ic_back,
    @DrawableRes rightIconRes: Int? = null,
    onLeftIconClick: () -> Unit = {},
    onRightIconClick: () -> Unit = {},
    titleText: String = "",
    lazyListSate: LazyListState? = null,
    modifier: Modifier = Modifier
) {

    val elevation = if (lazyListSate?.isScrolled == true) 6.dp else 0.dp

    var animate by remember { mutableStateOf(true) }
    val buttonAnimationProgress by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(500)
    )
    LaunchedEffect(lazyListSate?.isScrolled == true) {
        animate = !animate
    }

    Surface(shadowElevation = elevation) {
        MotionLayout(
            start = ConstraintSet(
                """
                {
                    icon: {
                        start: ['parent', 'start', 7],
                        top: ['parent', 'top', 7]
                    },
                    rightIcon: {
                        end: ['parent', 'end', 17],
                        top: ['parent', 'top', 7]
                    },
                    title: {
                        start: ['parent', 'start', 16],
                        top: ['parent', 'top', 50]
                    }
                }
            """
            ),
            end = ConstraintSet(
                """
                {
                    icon: {
                        height: 41,
                        start: ['parent', 'start', 7],
                        top: ['parent', 'top', 7],
                        bottom: ['parent', 'bottom', 7]
                    },
                    rightIcon: {
                        end: ['parent', 'end', 17],
                        top: ['parent', 'top', 7]
                    },
                    title: {
                        start: ['parent', 'start', 0],
                        end: ['parent', 'end', 0],
                        top: ['parent', 'top', 0],
                        bottom: ['parent', 'bottom', 0],
                    }
                }
            """
            ),
            progress = buttonAnimationProgress,
            modifier = modifier
                .fillMaxWidth()
                .background(White)
        ) {
            leftIconRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "leftIcon",
                    modifier = Modifier
                        .nonRippleClickable {
                            onLeftIconClick()
                        }
                        .layoutId("icon")
                )
            }
            rightIconRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "rightIcon",
                    modifier = Modifier
                        .nonRippleClickable {
                            onRightIconClick()
                        }
                        .layoutId("rightIcon")
                )
            }
            Text(
                text = titleText,
                style = if (lazyListSate?.isScrolled == true) Typography.body1 else Typography.subtitle1,
                modifier = Modifier.layoutId("title")
            )
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
    horizontalPadding: Dp = 20.dp,
    verticalPadding: Dp = 8.dp,
    buttonColor: Color = MainColor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isOutline) Color.Transparent else buttonColor,
            disabledContainerColor = Gray,
            disabledContentColor = White
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(round),
        border = BorderStroke(1.dp, if (isOutline) MainColor else Color.Transparent),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = Typography.body1,
            color = textColor,
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
                    .fillMaxWidth()
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
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusedIndicatorColor: Color = MainColor,
    unfocusedIndicatorColor: Color = Gray,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions(),
    trailingIcons: List<Int> = listOf(),
    supportText: String = "",
    errorText: String = "",
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }
    val color = if (isError) HotColor else if (isFocused) focusedIndicatorColor else unfocusedIndicatorColor

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
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
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    isError = isError,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = White
                    ),
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
                    },
                    trailingIcon = {
                        Row {
                            trailingIcons.forEach {
                                when (it) {
                                    R.drawable.ic_care -> {
                                        if (isError) Image(
                                            painter = painterResource(id = it),
                                            contentDescription = null
                                        )
                                    }
                                    else -> Image(
                                        painter = painterResource(id = it),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                )
            }
        )
        if (isError) {
            Text(text = errorText, style = Typography.caption, color = HotColor, modifier = Modifier.padding(top = 9.dp))
        } else {
            Text(text = supportText, style = Typography.caption, modifier = Modifier.padding(top = 9.dp))
        }
    }
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomTabRow(
    pagerState: PagerState,
    tabItems: List<String>,
    coroutineScope: CoroutineScope
) {
    Surface(
        shadowElevation = 6.dp,
        modifier = Modifier
            .background(White)
            .padding(bottom = 6.dp)
            .drawWithContent {
                val paddingPx = 6.dp.toPx()
                clipRect(
                    left = -paddingPx,
                    top = 0f,
                    right = size.width + paddingPx,
                    bottom = size.height + paddingPx
                ) {
                    this@drawWithContent.drawContent()
                }
            }
    ) {
        androidx.compose.material.ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = White,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, it),
                    color = MainColor,
                )
            },
            divider = {},
            edgePadding = 0.dp
        ) {
            tabItems.forEachIndexed { index, value ->
                CommonTab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    unselectedContentColor = DarkGray,
                    selectedContentColor = Black,
                ) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(vertical = 14.dp, horizontal = 23.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CommonTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {

    TabTransition(selectedContentColor, unselectedContentColor, selected) {
        Column(
            modifier = modifier
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = null
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
private fun TabTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(selected, label = "")
    val color by transition.animateColor(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(
                    durationMillis = 150,
                    delayMillis = 100,
                    easing = LinearEasing
                )
            } else {
                tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            }
        }, label = ""
    ) {
        if (it) activeColor else inactiveColor
    }
    CompositionLocalProvider(
        LocalContentColor provides color,
        content = content
    )
}