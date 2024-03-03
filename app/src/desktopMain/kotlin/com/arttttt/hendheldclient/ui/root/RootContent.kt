package com.arttttt.hendheldclient.ui.root

import GamepadKeyEvent
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.awt.Robot
import java.awt.event.KeyEvent

@Composable
fun RootContent(
    component: RootComponent,
    gamepadEventsFlow: Flow<GamepadKeyEvent>,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val stack by component.stack.subscribeAsState()

    val robot = remember {
        Robot()
    }

    Children(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { event ->
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

    LaunchedEffect(Unit) {
        gamepadEventsFlow
            .onEach { event ->
                when {
                    event.button == GamepadButton.ARROW_UP && event.direction == ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_UP)
                    }
                    event.button == GamepadButton.ARROW_UP && event.direction == ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_UP)
                    }
                    event.button == GamepadButton.ARROW_DOWN && event.direction == ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_DOWN)
                    }
                    event.button == GamepadButton.ARROW_DOWN && event.direction == ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_DOWN)
                    }
                    event.button == GamepadButton.BUTTON_A && event.direction == ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_ENTER)
                    }
                    event.button == GamepadButton.BUTTON_A && event.direction == ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_ENTER)
                    }
                    event.button == GamepadButton.BUTTON_B && event.direction == ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_ESCAPE)
                    }
                    event.button == GamepadButton.BUTTON_B && event.direction == ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_ESCAPE)
                    }
                }
            }
            .launchIn(this)
    }
}