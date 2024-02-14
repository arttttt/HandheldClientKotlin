package com.arttttt.hendheldclient.components.main

import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.ui.main.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainComponentImpl(
    context: AppComponentContext,
) : MainComponent,
    AppComponentContext by context {

    override val states = MutableStateFlow(
        MainComponent.UiState(
            navigationItems = listOf(
                NavigationItem.HHD,
                NavigationItem.About,
            ),
            selectedItem = NavigationItem.HHD,
        )
    )

    override fun onItemClicked(item: NavigationItem) {
        states.update { state ->
            state.copy(
                selectedItem = item,
            )
        }
    }
}