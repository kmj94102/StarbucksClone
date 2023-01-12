package com.example.starbucksclone.view.main.order.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.starbucksclone.R
import com.example.starbucksclone.ui.theme.MainColor
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.nonRippleClickable
import com.example.starbucksclone.view.common.RoundedButton

@Composable
fun CartAdditionCompleteBottomSheet(
    goToCartListener: () -> Unit,
    finishListener: () -> Unit,
    closeListener: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cart_added),
                style = getTextStyle(18, true)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.nonRippleClickable { closeListener() }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            RoundedButton(
                text = stringResource(id = R.string.go_to_cart),
                isOutline = true,
                textColor = MainColor,
                modifier = Modifier.weight(1f)
            ) {
                goToCartListener()
            }
            RoundedButton(
                text = stringResource(id = R.string.more_other_menus),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                finishListener()
            }
        }
    }
}