package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            .padding(8.dp)
    ) {

        ModeDropDown(
            title = item.title,
            selectedMode = item.mode.title,
            modes = item.modes,
            onValueChanged = { value ->
                onValueChanged.invoke(
                    item.id,
                    value,
                )
            },
        )

        Spacer(Modifier.height(16.dp))

        item.mode.children.forEach { child ->
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
                        item = child,
                        onValueChanged = { value ->
                            onValueChanged.invoke(
                                child.id,
                                value
                            )
                        },
                    )
                }
                is MultipleListItem -> {
                    MultipleItemContent(
                        item = child,
                        onValueChanged = { value ->
                            onValueChanged.invoke(
                                child.id,
                                value
                            )
                        },
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ModeDropDown(
    title: String,
    selectedMode: String,
    modes: Map<FieldKey, String>,
    onValueChanged: (Any) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            modifier = Modifier,
            value = selectedMode,
            onValueChange = {},
            readOnly = true,
            label = { Text(title) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            modes.forEach { (key, value) ->
                DropdownMenuItem(
                    onClick = {
                        isExpanded = false
                        onValueChanged.invoke(key.key)
                    },
                ) {
                    Text(value)
                }
            }
        }
    }
}