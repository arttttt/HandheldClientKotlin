package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem

@Composable
fun BooleanItemContent(
    item: BooleanListItem,
    onValueChanged: (Any) -> Unit
) {
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
            onCheckedChange = onValueChanged,
        )
    }
}