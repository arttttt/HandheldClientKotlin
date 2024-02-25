package com.arttttt.hendheldclient.components.hhd

import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.utils.ListItem
import kotlinx.coroutines.flow.StateFlow

interface HhdComponent : DecomposeComponent {

    data class UiState(
        val items: List<ListItem>
    )

    val states: StateFlow<UiState>

    fun onValueUpdated(
        key: FieldKey,
        value: Any,
    )

    fun onResetClicked(
        key: FieldKey
    )
}