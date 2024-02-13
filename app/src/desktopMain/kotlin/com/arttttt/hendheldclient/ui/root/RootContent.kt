package com.arttttt.hendheldclient.ui.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arttttt.hendheldclient.components.login.LoginComponent
import com.arttttt.hendheldclient.components.root.RootComponent
import com.arttttt.hendheldclient.ui.login.LoginContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack,
    ) {
        when (val child = it.instance) {
            is LoginComponent -> LoginContent(child)
        }
    }
}