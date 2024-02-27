package com.arttttt.hendheldclient.ui.hhd

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.ui.hhd.list.content.ContainerItemContent
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.utils.ListItem

@Composable
fun HhdContent(component: HhdComponent) {
    val state by component.states.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 72.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = state.items,
                key = ListItem::key,
            ) { item ->
                when (item) {
                    is ContainerListItem -> ContainerItemContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .border(
                                width = 1.dp,
                                color = Color.Red,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        item = item,
                        onValueChanged = component::onValueUpdated,
                        onResetClicked = component::onResetClicked,
                    )
                }
            }
        }

        /**
         * todo: animated appearance
         */
        if (state.areActionsVisible) {
            ActionsRow(
                modifier = Modifier.align(Alignment.BottomCenter),
                onApplyClicked = component::onApplyClicked,
                onResetClicked = component::onResetAllClicked,
            )
        }
    }
}

@Composable
private fun ActionsRow(
    modifier: Modifier,
    onApplyClicked: () -> Unit,
    onResetClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp
            )
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .background(Color.Gray)
            .padding(
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = onApplyClicked
        ) {
            Text("apply")
        }

        Button(
            onClick = onResetClicked
        ) {
            Text("reset")
        }
    }
}