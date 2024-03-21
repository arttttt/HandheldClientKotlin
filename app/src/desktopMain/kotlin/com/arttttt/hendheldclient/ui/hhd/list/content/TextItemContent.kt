package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem

@Composable
fun TextItemContent(
    modifier: Modifier,
    item: TextListItem,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(
                minHeight = ButtonDefaults.MinHeight,
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(item.title)

        if (item.value != null) {
            Spacer(Modifier.width(8.dp))

            Text(item.value)
        }
    }
}