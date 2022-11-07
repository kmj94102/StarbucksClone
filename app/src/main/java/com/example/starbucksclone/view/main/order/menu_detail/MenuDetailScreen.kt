package com.example.starbucksclone.view.main.order.menu_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.database.entity.MenuDetailEntity
import com.example.starbucksclone.ui.theme.*
import com.example.starbucksclone.util.Constants
import com.example.starbucksclone.util.getColorFromHexCode
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun MenuDetailScreen(
    routAction: RoutAction,
    viewModel: MenuDetailViewModel = hiltViewModel()
) {
    val item = viewModel.item.value
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(getColorFromHexCode(code = viewModel.color))
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = "menu image",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp)
                )
            }
        }
        item {
            ItemInfo(item)
        }

        item {
            Spacer(modifier = Modifier.height(2.dp))

            when (viewModel.type.value) {
                Constants.HotAndIced -> {
                    HotAndIceSelector(
                        isHotSelected = viewModel.selectType.value,
                    ) {
                        viewModel.event(MenuDetailEvent.TypeSelect(it))
                    }
                }
                Constants.HotOnly -> {
                    OnlySelector(isHotOnly = true, type = Constants.HotOnly)
                }
                Constants.IcedOnly -> {
                    OnlySelector(isHotOnly = false, type = Constants.IcedOnly)
                }
            }
        }
    }
}

@Composable
fun ItemInfo(item: MenuDetailEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp, vertical = 20.dp)
    ) {
        Text(text = item.name, style = Typography.subtitle1)
        Spacer(modifier = Modifier.height(5.dp))

        Text(text = item.nameEng, style = Typography.caption)
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = item.description,
            style = Typography.caption,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "4,400원", style = Typography.subtitle2)
    }
}

@Composable
fun HotAndIceSelector(
    isHotSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        OutlinedButton(
            border = if (isHotSelected)
                BorderStroke(1.dp, HotColor)
            else
                BorderStroke(1.dp, BorderColor),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                bottomStart = 20.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isHotSelected) HotColor else White
            ),
            contentPadding = PaddingValues(vertical = 9.dp),
            onClick = { onClick(true) },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "HOT", style = Typography.body2,
                color = if (isHotSelected) White else DarkGray
            )
        }

        OutlinedButton(
            border = if (isHotSelected)
                BorderStroke(1.dp, BorderColor)
            else
                BorderStroke(1.dp, IceColor),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 20.dp,
                bottomEnd = 20.dp
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isHotSelected) White else IceColor
            ),
            contentPadding = PaddingValues(vertical = 9.dp),
            onClick = { onClick(false) },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "ICED", style = Typography.body2,
                color = if (isHotSelected) DarkGray else White
            )
        }
    }
}

@Composable
fun OnlySelector(
    isHotOnly: Boolean,
    type: String
) {
    OutlinedCard(
        border = BorderStroke(1.dp, BorderColor),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Text(
            text = type, style = Typography.body2,
            color = if (isHotOnly) HotColor else IceColor,
            modifier = Modifier
                .padding(vertical = 9.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}