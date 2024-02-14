package com.arttttt.hendheldclient.ui.main

import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arttttt.hendheldclient.components.main.MainComponent

@Composable
fun MainContent(component: MainComponent) {
    val state by component.states.collectAsState()

    NavigationContent(
        items = state.navigationItems,
        selectedItem = state.selectedItem,
        onItemClicked = component::onItemClicked,
    )
}

@Composable
private fun NavigationContent(
    items: List<NavigationItem>,
    selectedItem: NavigationItem,
    onItemClicked: (NavigationItem) -> Unit,
) {
    NavigationRail {
        items.forEach { item ->
            NavigationRailItem(
                selected = item == selectedItem,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(item.title)
                },
                onClick = {
                    onItemClicked.invoke(item)
                },
            )
        }
    }
}