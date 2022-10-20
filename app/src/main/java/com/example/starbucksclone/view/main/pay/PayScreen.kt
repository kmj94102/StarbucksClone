package com.example.starbucksclone.view.main.pay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.starbucksclone.view.common.MotionTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PayScreen() {
    val listState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {


        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            stickyHeader {
                MotionTitle(
                    onLeftIconClick = { },
                    leftIconRes = null,
                    lazyListSate = listState,
                    titleText = "Pay"
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize()
                ) {

                }
            }
        }
    }

}