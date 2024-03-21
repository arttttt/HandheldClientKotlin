@file:OptIn(ExperimentalMaterialApi::class)

package com.arttttt.hendheldclient.utils

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.semantics.clearAndSetSemantics

@Composable
fun ExposedDropdownMenuDefaults.NonFocusableTrailingIcon(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    TrailingIcon(
        modifier = modifier
            .focusProperties {
                canFocus = false
            },
        expanded = expanded,
        onClick = onClick,
    )
}

@Suppress("UnusedReceiverParameter")
@Composable
fun ExposedDropdownMenuDefaults.TrailingIcon(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .clearAndSetSemantics { }
            .then(modifier),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier
                .rotate(
                    if (expanded)
                        180f
                    else
                        360f
                ),
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
        )
    }
}