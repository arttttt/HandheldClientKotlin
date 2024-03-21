package com.arttttt.hendheldclient.ui.hhd.list.content

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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import com.arttttt.hendheldclient.ui.hhd.list.model.DiscreteListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.MultipleListItem
import com.arttttt.hendheldclient.utils.NonFocusableTrailingIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultipleItemContent(
    modifier: Modifier,
    item: MultipleListItem,
    onValueChanged: (Any) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .onKeyEvent { event ->
                when {
                    event.key == Key.Enter && event.type == KeyEventType.KeyDown -> true
                    event.key == Key.Enter && event.type == KeyEventType.KeyUp -> {
                        isExpanded = true

                        true
                    }

                    event.key == Key.Escape && event.type == KeyEventType.KeyDown -> true
                    event.key == Key.Escape && event.type == KeyEventType.KeyUp -> {
                        isExpanded = false

                        true
                    }

                    else -> false
                }
            },
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            modifier = Modifier,
            value = item.value,
            onValueChange = {},
            readOnly = true,
            label = { Text(item.title) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.NonFocusableTrailingIcon(
                    expanded = isExpanded,
                    onClick = {
                        isExpanded = !isExpanded
                    },
                )
            }
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            item.values.entries.forEach { (_, value) ->
                DropdownMenuItem(
                    onClick = {
                        isExpanded = false
                        onValueChanged.invoke(value)
                    },
                ) {
                    Text(value)
                }
            }
        }
    }
}