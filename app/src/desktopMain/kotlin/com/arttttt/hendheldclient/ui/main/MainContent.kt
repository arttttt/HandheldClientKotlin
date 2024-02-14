package com.arttttt.hendheldclient.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.components.main.MainComponent
import com.arttttt.hendheldclient.ui.hhd.HhdContent

@Composable
fun MainContent(component: MainComponent) {
    val state by component.states.collectAsState()

    NavigationContent(
        items = state.navigationItems,
        selectedItem = state.selectedItem,
        stack = component.stack,
        onItemClicked = component::onItemClicked,
    )
}

@Composable
private fun NavigationContent(
    items: List<NavigationItem>,
    selectedItem: NavigationItem,
    stack: Value<ChildStack<*, DecomposeComponent>>,
    onItemClicked: (NavigationItem) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavigationRail {
            items.forEach { item ->
                NavigationItem(
                    icon = item.icon,
                    title = item.title,
                    isSelected = selectedItem == item,
                    onItemClicked = {
                        onItemClicked.invoke(item)
                    },
                )
            }
        }

        Children(
            stack = stack,
        ) {
            when (val child = it.instance) {
                is HhdComponent -> HhdContent(child)
            }
        }
    }
}

@Composable
private fun NavigationItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onItemClicked: () -> Unit
) {
    NavigationRailItem(
        selected = isSelected,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        label = {
            Text(title)
        },
        onClick = onItemClicked,
    )
}