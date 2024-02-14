package com.arttttt.hendheldclient.components.main

import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.ui.main.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface MainComponent : DecomposeComponent {

    data class UiState(
        val navigationItems: List<NavigationItem>,
        val selectedItem: NavigationItem,
    )

    val states: StateFlow<UiState>

    fun onItemClicked(item: NavigationItem)
}