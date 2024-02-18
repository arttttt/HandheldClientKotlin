package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem

@Composable
fun IntInputItemContent(
    item: IntListItem,
    onValueChanged: (Any) -> Unit,
    onResetClicked: () -> Unit,
) {
    OutlinedTextField(
        value = item.value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = item.title
            )
        },
        isError = item.error != null,
        trailingIcon = {
            if (item.isValueOverwritten) {
                IconButton(
                    onClick = onResetClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        )
    )
}