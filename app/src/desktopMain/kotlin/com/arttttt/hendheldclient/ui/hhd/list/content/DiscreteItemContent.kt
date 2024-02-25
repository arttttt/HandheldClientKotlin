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
import com.arttttt.hendheldclient.ui.hhd.list.model.DiscreteListItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiscreteItemContent(
    item: DiscreteListItem
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            modifier = Modifier,
            value = item.values.elementAt(selectedIndex).toString(),
            onValueChange = { },
            readOnly = true,
            label = { Text(item.title) },
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
            item.values.forEachIndexed { index, value ->
                DropdownMenuItem(
                    onClick = {
                        isExpanded = false
                        selectedIndex = index
                    },
                ) {
                    Text(value.toString())
                }
            }
        }
    }
}