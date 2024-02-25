package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.DiscreteListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ModeListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.MultipleListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem

@Composable
fun ModeItemContent(
    modifier: Modifier,
    item: ModeListItem,
    onValueChanged: (FieldKey, Any) -> Unit,
    onResetClicked: (FieldKey) -> Unit,
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
                is BooleanListItem -> {
                    BooleanItemContent(
                        item = child,
                        onValueChanged = { value ->
                            onValueChanged.invoke(
                                child.id,
                                value,
                            )
                        }
                    )
                }
                is IntListItem -> {
                    IntInputItemContent(
                        item = child,
                        onValueChanged = { value ->
                            onValueChanged.invoke(
                                child.id,
                                value,
                            )
                        },
                        onResetClicked = {
                            onResetClicked.invoke(child.id)
                        },
                    )
                }
                is DiscreteListItem -> {
                    DiscreteItemContent(
                        item = child
                    )
                }
                is MultipleListItem -> {
                    MultipleItemContent(
                        item = child,
                    )
                }
                is ContainerListItem -> {
                    ContainerItemContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        item = child,
                        onValueChanged = onValueChanged,
                        onResetClicked = onResetClicked,
                    )
                }
            }
        }
    }
}