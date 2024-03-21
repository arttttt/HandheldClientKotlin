package com.arttttt.hendheldclient.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.drawBorderWhenFocused(): Modifier {
    return composed {
        var isFocused by remember { mutableStateOf(false) }

        this
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .then(
                if (isFocused) {
                    Modifier.border(1.5.dp, Color.Blue, RoundedCornerShape(8.dp))
                } else {
                    Modifier
                }
            )
    }
}