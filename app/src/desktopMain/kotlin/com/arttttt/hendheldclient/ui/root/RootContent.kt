package com.arttttt.hendheldclient.ui.root

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.hendheldclient.components.login.LoginComponent
import com.arttttt.hendheldclient.components.main.MainComponent
import com.arttttt.hendheldclient.components.root.RootComponent
import com.arttttt.hendheldclient.ui.login.LoginContent
import com.arttttt.hendheldclient.ui.main.MainContent

@Composable
fun RootContent(component: RootComponent) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val stack by component.stack.subscribeAsState()

    Children(
        modifier = Modifier
            .focusable()
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { event ->
                println(event)

                when {
                    event.key == Key.DirectionUp && KeyEventType.KeyUp == event.type -> {
                        focusManager.moveFocus(FocusDirection.Previous)
                        true
                    }
                    event.key == Key.DirectionDown && KeyEventType.KeyUp == event.type -> {
                        focusManager.moveFocus(FocusDirection.Next)
                        true
                    }

                    else -> false
                }
            },
        stack = stack,
    ) {
        when (val child = it.instance) {
            is LoginComponent -> LoginContent(child)
            is MainComponent -> MainContent(child)
        }
    }

    LaunchedEffect(stack.active.instance) {
        focusRequester.requestFocus()
    }
}