package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.utils.drawBorderWhenFocused

@Composable
fun BooleanItemContent(
    modifier: Modifier,
    item: BooleanListItem,
    onValueChanged: (Any) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBorderWhenFocused()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onValueChanged(!item.isChecked)
                }
            )
            .then(modifier)
            .focusable(true),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .focusable(false),
            text = item.title
        )

        Spacer(Modifier.width(8.dp))

        Switch(
            modifier = Modifier.focusable(false),
            checked = item.isChecked,
            onCheckedChange = onValueChanged,
        )
    }
}