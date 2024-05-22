package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.text.style.TextAlign
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem

@Composable
fun ActionItemContent(
    modifier: Modifier,
    item: ActionListItem,
) {
    Button(
        modifier = modifier.focusable(item.isEnabled),
        onClick = {},
        enabled = item.isEnabled,
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .focusable(false),
            text = item.title,
            textAlign = TextAlign.Center,
        )
    }
}