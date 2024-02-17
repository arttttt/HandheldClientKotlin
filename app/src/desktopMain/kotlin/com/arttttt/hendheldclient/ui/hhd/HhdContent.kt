package com.arttttt.hendheldclient.ui.hhd

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem
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
                        .wrapContentHeight(),
                    item = item,
                    onValueChanged = { parent, id, value ->}
                )
            }
        }
    }
}

@Composable
private fun ContainerItemContent(
    modifier: Modifier,
    item: ContainerListItem,
    onValueChanged: (String, String, String) -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Red,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = item.title
        )

        Spacer(Modifier.height(16.dp))

        item.children.forEach { child ->
            when (child) {
                is TextListItem -> TextItemContent(child)
                is ActionListItem -> ActionItemContent(child)
                is BooleanListItem -> BooleanItemContent(child)
                is IntListItem -> {
                    IntItemContent(
                        item = child,
                        onValueChanged = { value ->
                            onValueChanged.invoke(
                                item.title,
                                child.title,
                                value,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TextItemContent(item: TextListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(
                minHeight = ButtonDefaults.MinHeight,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(item.title)

        if (item.value != null) {
            Spacer(Modifier.width(8.dp))

            Text(item.value)
        }
    }
}

@Composable
private fun ActionItemContent(item: ActionListItem) {
    Button(
        modifier = Modifier,
        onClick = {},
        enabled = item.isEnabled,
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = item.title,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun BooleanItemContent(item: BooleanListItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.title
        )

        Spacer(Modifier.width(8.dp))

        Switch(
            checked = item.isChecked,
            onCheckedChange = { newValue -> }
        )
    }
}

@Composable
private fun IntItemContent(
    item: IntListItem,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = item.value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = item.title
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        )
    )
}