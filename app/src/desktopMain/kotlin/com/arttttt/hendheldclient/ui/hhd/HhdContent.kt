package com.arttttt.hendheldclient.ui.hhd

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.ui.hhd.list.content.ContainerItemContent
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.utils.ListItem

@Composable
fun HhdContent(component: HhdComponent) {
    val state by component.states.collectAsState()

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(
            items = state.items,
            key = ListItem::key,
            span = {
                StaggeredGridItemSpan.SingleLane
            }
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
}