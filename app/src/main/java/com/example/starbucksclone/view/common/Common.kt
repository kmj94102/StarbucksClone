package com.example.starbucksclone.view.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import coil.compose.AsyncImage
import com.example.starbucksclone.R
import com.example.starbucksclone.database.entity.CardInfo
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.toPriceFormat
import com.example.starbucksclone.util.toSecretFormat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 공용 타이틀
 * @param leftIconRes 좌측 아이콘 Res
 * @param rightContents 우측 영역 컨텐츠, Composable 형식
 * @param onLeftIconClick 좌측 아이콘 클릭 리스너
 * @param titleText 타이틀 텍스트
 * @param isExpand 타이틀 영역 확장 여부
 * @param isShadowVisible 타이틀 영역이 축소되어있을 때 그림자 여부
 * @param modifier Modifier
 * **/
@Composable
fun MainTitle(
    @DrawableRes leftIconRes: Int? = R.drawable.ic_back,
    rightContents: @Composable (BoxScope.() -> Unit)? = null,
    onLeftIconClick: () -> Unit = {},
    titleText: String,
    isExpand: Boolean = true,
    isShadowVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = if (isShadowVisible && isExpand.not()) 6.dp else 0.dp,
        color = White,
        modifier = modifier
    ) {
        val alignment by animateAlignmentAsState(
            if (isExpand) Alignment.TopStart else Alignment.TopCenter
        )
        val paddingTop by animateDpAsState(
            if (isExpand) 51.dp else 10.dp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 42.dp)
        ) {
            leftIconRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 9.dp, start = 8.dp, bottom = 9.dp)
                        .nonRippleClickable { onLeftIconClick() }
                )
            }
            rightContents?.let { it() }
            Text(
                text = titleText,
                style = if (isExpand) Typography.subtitle1 else Typography.body1,
                modifier = Modifier
                    .align(alignment)
                    .padding(top = paddingTop, start = 16.dp, end = 16.dp)
            )
        }
    }
}

/**
 * Alignment 상태 변화 애니메이션
 * **/
@Composable
fun animateAlignmentAsState(
    targetAlignment: Alignment,
): State<Alignment> {
    val biased = targetAlignment as BiasAlignment
    val horizontal by animateFloatAsState(biased.horizontalBias)
    val vertical by animateFloatAsState(biased.verticalBias)
    return derivedStateOf { BiasAlignment(horizontal, vertical) }
}

/**
 * 라운드 버튼
 * @param text [필수] 버튼 텍스트
 * @param textColor 텍스트 색상
 * @param textStyle 텍스트 스타일
 * @param round 버튼 라운드 값
 * @param isEnabled 활성화 여부
 * @param isOutline 아웃라인 형식 여부
 * @param padding 패딩
 * @param buttonColor 버튼 색상
 * @param modifier Modifier
 * @param onClick 버튼 클릭 리스너
 * **/
@Composable
fun RoundedButton(
    text: String,
    textColor: Color = White,
    textStyle: TextStyle = getTextStyle(16),
    round: Dp = 20.dp,
    isEnabled: Boolean = true,
    isOutline: Boolean = false,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
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
        border = BorderStroke(1.dp, if (isOutline) buttonColor else Color.Transparent),
        contentPadding = padding,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

/**
 * 버튼 모양의 라디오 버튼
 * @param isSelected 선택 여부
 * @param text 버튼 텍스트
 * @param textStyle 텍스트 스타일
 * @param unselectedTextColor 미선택된 텍스트 색상
 * @param selectedTextColor 선택된 텍스트
 * @param unselectedColor 미선택된 버튼 보더 색상
 * @param selectedColor 선택된 버튼 색상
 * @param padding 패딩
 * @param modifier Modifier
 * @param onClick 버튼 클릭 리스너
 * **/
@Composable
fun SelectButton(
    isSelected: Boolean,
    text: String,
    textStyle: TextStyle = getTextStyle(14, true),
    unselectedTextColor: Color = DarkGray,
    selectedTextColor: Color = White,
    unselectedColor: Color = BorderColor,
    selectedColor: Color = MainColor,
    padding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 11.dp),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) selectedColor else Color.Transparent
        ),
        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else unselectedColor),
        contentPadding = padding,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (isSelected) selectedTextColor else unselectedTextColor
        )
    }
}

/**
 * 분활 선택 버튼
 * @param contentList SegmentButton 에 사용할 버튼 리스트
 * @param selectedValue 선택 된 아이템
 * @param round 좌우측 라운드값
 * @param modifier Modifier
 * @param selectedChangeListener 선택 변경 리스너
 * **/
@Composable
fun SegmentButton(
    contentList: List<String>,
    selectedValue: String,
    round: Dp = 20.dp,
    modifier: Modifier = Modifier,
    selectedChangeListener: (String) -> Unit
) {
    OutlinedCard(
        border = BorderStroke(1.dp, Color(0xFFAAAAAA)),
        shape = RoundedCornerShape(round),
        modifier = modifier.height(35.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            contentList.forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (selectedValue == it) MainColor else Color.Transparent)
                        .border(BorderStroke(0.5.dp, Color(0xFFAAAAAA)))
                        .nonRippleClickable {
                            selectedChangeListener(it)
                        }
                ) {
                    Text(
                        text = it,
                        color = if (selectedValue == it) White else Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        }
    }
}

/**
 * 하단 버튼
 * @param text [필수] 버튼 문구
 * @param round 버튼 라운드 값
 * @param isEnabled 버튼 활성화 여부
 * @param buttonColor 버튼 색상
 * @param buttonModifier 버튼 Modifier
 * @param onClick 버튼 클릭 리스너
 * @param modifier Modifier
 * **/
@Composable
fun FooterWithButton(
    text: String,
    round: Dp = 20.dp,
    isEnabled: Boolean = true,
    buttonColor: Color = MainColor,
    buttonModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shadowElevation = 15.dp,
        color = White,
        modifier = modifier
    ) {
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
    trailingIcons: @Composable () -> Unit = {},
    supportText: String = "",
    errorText: String = "",
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }
    val color =
        if (isError) HotColor else if (isFocused) focusedIndicatorColor else unfocusedIndicatorColor

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
                    visualTransformation = visualTransformation,
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
                            Text(
                                text = hint,
                                style = Typography.body1,
                                fontSize = 14.sp,
                                color = Black
                            )
                        }
                    } else {
                        null
                    },
                    trailingIcon = {
                        trailingIcons()
                    }
                )
            }
        )
        if (isError) {
            Text(
                text = errorText,
                style = Typography.caption,
                color = HotColor,
                modifier = Modifier.padding(top = 9.dp)
            )
        } else {
            Text(
                text = supportText,
                style = Typography.caption,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

/**
 * 커스텀 체크 박스
 * @param text 체크박스 문구
 * @param textStyle 첵크박스 문구 스타일
 * @param selected 체크박스 선택 여부
 * @param onClick 체크박스 클릭 리스너
 * @param checkedIcon 체크박스 선택된 체크박스 아이콘
 * @param uncheckedIcon 체크박스 선택 안된 체크박스 아이콘
 * @param isNextButton 체크박스 오른쪽에 더보기 버튼 여부
 * @param onNextClick 더보기 버튼 클릭 리스너
 * @param modifier Modifier
 * **/
@Composable
fun CustomCheckBox(
    text: String,
    textStyle: TextStyle = getTextStyle(16),
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes checkedIcon: Int = R.drawable.ic_radio_check,
    @DrawableRes uncheckedIcon: Int = R.drawable.ic_radio_unchecked,
    isNextButton: Boolean = false,
    nextIconTint: Color = DarkGray,
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
            painter = painterResource(id = if (selected) checkedIcon else uncheckedIcon),
            contentDescription = "checkbox"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .weight(1f)
        )
        if (isNextButton) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = "next",
                tint = nextIconTint,
                modifier = Modifier.nonRippleClickable {
                    onNextClick?.invoke()
                }
            )
        }
    }
}

/**
 * 커스텀 체크 박스
 * @param text 체크박스 문구
 * @param textStyle 첵크박스 문구 스타일
 * @param selected 체크박스 선택 여부
 * @param onClick 체크박스 클릭 리스너
 * @param checkedIcon 체크박스 선택된 체크박스 아이콘
 * @param uncheckedIcon 체크박스 선택 안된 체크박스 아이콘
 * @param isNextButton 체크박스 오른쪽에 더보기 버튼 여부
 * @param onNextClick 더보기 버튼 클릭 리스너
 * @param modifier Modifier
 * **/
@Composable
fun CustomCheckBox(
    text: String,
    textStyle: TextStyle = getTextStyle(16),
    selected: Boolean,
    onClick: () -> Unit,
    checkedIcon: @Composable () -> Unit,
    uncheckedIcon: @Composable () -> Unit,
    isNextButton: Boolean = false,
    nextIconTint: Color = DarkGray,
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
        if (selected) {
            checkedIcon()
        } else {
            uncheckedIcon()
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .weight(1f)
        )
        if (isNextButton) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = "next",
                tint = nextIconTint,
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
    indicatorColor: Color = MainColor,
    coroutineScope: CoroutineScope,
    isShadowVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = if (isShadowVisible) 6.dp else 0.dp,
        modifier = modifier
            .background(White)
            .padding(bottom = if (isShadowVisible) 6.dp else 0.dp)
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
                    color = indicatorColor,
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

/**
 * 프로그래스바
 * @param current 프로그래스의 현재 값
 * @param max 프로그래스의 최대 값
 * @param modifier Modifier
 * **/
@Composable
fun Progressbar(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    val isStart = remember { mutableStateOf(false) }
    val progress = animateFloatAsState(
        targetValue = if (isStart.value) current.toFloat() / max else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        isStart.value = true
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(8.dp)
            .background(LightGray)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .height(8.dp)
                .fillMaxWidth(progress.value)
                .background(MainColor)
        )
    }
}

/**
 * 원형 이미지
 * @param imageURL 이미지 URL
 * @param backgroundColor 원 배경
 * @param size 원 사이즈
 * @param modifier Modifier
 * **/
@Composable
fun CircleImage(
    imageURL: String,
    backgroundColor: Color = White,
    size: Dp = 126.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .size(size)
    ) {
        AsyncImage(
            model = imageURL,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size)
        )
    }
}

/**
 * 카드 아이템
 * @param cardInfo 카드 정보
 * @param isBigSize 큰사이 즈여부
 * @param isRepresentativeVisible 대표카드 아이콘 표시 여부
 * @param isCardNumberVisible 카드 번호 표시 여부
 * **/
@Composable
fun CardItem(
    cardInfo: CardInfo,
    title: String = "",
    isBigSize: Boolean = true,
    isRepresentativeVisible: Boolean = false,
    isCardNumberVisible: Boolean = false,
    representativeClickListener: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val with = if (isBigSize) 115.dp else 56.dp
    val height = if (isBigSize) 72.dp else 35.dp

    Row(modifier = modifier) {
        AsyncImage(
            model = cardInfo.image,
            contentDescription = "starbucks card",
            modifier = Modifier.size(with, height)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = if (isBigSize) 13.dp else 9.dp)
        ) {
            Text(text = title.ifEmpty { cardInfo.name }, style = getTextStyle(12))
            Spacer(modifier = Modifier.height(if (isBigSize) 7.dp else 3.dp))
            Text(
                text = cardInfo.balance.toPriceFormat(),
                style = getTextStyle(
                    size = if (isBigSize) 20 else 16,
                    isBold = true
                )
            )
            if (isCardNumberVisible) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = cardInfo.cardNumber.toSecretFormat(),
                    style = getTextStyle(size = 12, color = DarkGray)
                )
            }
        }

        if (isRepresentativeVisible) {
            Image(
                painter = painterResource(
                    if (cardInfo.representative) {
                        R.drawable.ic_star_circle_selected
                    } else {
                        R.drawable.ic_star_circle
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(41.dp)
                    .align(Alignment.CenterVertically)
                    .nonRippleClickable {
                        if (cardInfo.representative.not()) {
                            representativeClickListener(cardInfo.cardNumber, cardInfo.name)
                        }
                    }
            )
        }
    }
}

/** 공용 바텀시트 다이얼로그 **/
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommonBottomSheetDialog(
    state: ModalBottomSheetState,
    dialogContents: @Composable () -> Unit,
    mainContents: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            dialogContents()
        }
    ) {
        mainContents()
    }
}

//val modalState = rememberModalBottomSheetState(
//    initialValue = ModalBottomSheetValue.Hidden,
//    skipHalfExpanded = false
//)
//val scope = rememberCoroutineScope()
//
//ModalBottomSheetLayout(
//    sheetState = modalState,
//    sheetContent = {
//        ModalBottomSheetContents()
//    }
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        ModalBottomSheetMain {
//            scope.launch {
//                modalState.show()
//            }
//        }
//    }
//}
//
//@Composable
//fun ModalBottomSheetMain(
//    onClickListener: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(vertical = 20.dp, horizontal = 24.dp)
//    ) {
//        Text(
//            text = "Modal Bottom Sheet",
//            style = Typography.h4,
//            fontWeight = FontWeight.Bold
//        )
//
//        Button(onClick = onClickListener) {
//            Text(text = "버튼", style = Typography.body1)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ModalBottomSheetContents() {
//    LazyColumn {
//        items(50) {
//            ListItem(
//                text = { Text("Item $it") },
//                icon = {
//                    Icon(
//                        Icons.Default.Favorite,
//                        contentDescription = "Localized description"
//                    )
//                }
//            )
//        }
//    }
//}