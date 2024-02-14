package com.arttttt.hendheldclient.components.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.ui.main.NavigationItem
import kotlinx.coroutines.flow.StateFlow

interface MainComponent : DecomposeComponent {

    data class UiState(
        val navigationItems: List<NavigationItem>,
        val selectedItem: NavigationItem,
    )

    val states: StateFlow<UiState>

    val stack: Value<ChildStack<*, DecomposeComponent>>

    fun onItemClicked(item: NavigationItem)
}