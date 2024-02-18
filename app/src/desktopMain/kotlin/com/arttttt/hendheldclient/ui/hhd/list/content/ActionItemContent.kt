package com.arttttt.hendheldclient.ui.hhd.list.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem

@Composable
fun ActionItemContent(item: ActionListItem) {
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