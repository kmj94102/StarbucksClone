package com.example.starbucksclone.view.main.order.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.starbucksclone.database.entity.MenuEntity
import com.example.starbucksclone.ui.theme.Black
import com.example.starbucksclone.ui.theme.Typography
import com.example.starbucksclone.util.getColorFromHexCode
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.util.priceFormat
import com.example.starbucksclone.view.common.MotionTitle
import com.example.starbucksclone.view.navigation.RoutAction

@Composable
fun MenuScreen(
    routAction: RoutAction,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        MotionTitle(
            titleText = viewModel.name.value,
            lazyListSate = lazyListState
        )

        LazyColumn(
            contentPadding = PaddingValues(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(23.dp),
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            viewModel.list.forEach {
                item {
                    MenuItem(it) {
                        routAction.goToOrderItem(it.indexes, it.type, it.color)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    drinkEntity: MenuEntity,
    onClick: (MenuEntity) -> Unit
) {
    val style = TextStyle(
        fontSize = 14.sp,
        color = Black,
        fontWeight = FontWeight.Bold
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
            .nonRippleClickable { onClick(drinkEntity) }
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(96.dp)
                .background(getColorFromHexCode(drinkEntity.color))
        ) {
            AsyncImage(
                model = drinkEntity.image,
                contentDescription = "image",
                modifier = Modifier.size(83.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp)
        ) {
            Text(text = drinkEntity.name, style = style)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = drinkEntity.nameEng, style = Typography.caption)
            Spacer(modifier = Modifier.height(9.dp))
            Text(text = drinkEntity.price.priceFormat(), style = style)
        }

    }
}