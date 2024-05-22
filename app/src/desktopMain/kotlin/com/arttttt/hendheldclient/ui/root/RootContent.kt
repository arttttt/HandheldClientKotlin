package com.arttttt.hendheldclient.ui.root

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
import com.arttttt.hendheldclient.utils.GamepadManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.awt.Robot
import java.awt.event.KeyEvent

/**
 * todo: move out key handling code
 */
@Composable
fun RootContent(
    component: RootComponent,
    gamepadEventsFlow: Flow<GamepadManager.GamepadKeyEvent>,
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
                        focusManager.moveFocus(FocusDirection.Up)
                        true
                    }
                    event.key == Key.DirectionDown && KeyEventType.KeyUp == event.type -> {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    }
                    event.key == Key.DirectionLeft && KeyEventType.KeyUp == event.type -> {
                        if (!focusManager.moveFocus(FocusDirection.Left)) {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                        true
                    }
                    event.key == Key.DirectionRight && KeyEventType.KeyUp == event.type -> {
                        if (!focusManager.moveFocus(FocusDirection.Right)) {
                            focusManager.moveFocus(FocusDirection.Right)
                        }
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
                    event.button == GamepadManager.GamepadButton.ARROW_UP && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_UP)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_UP && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_UP)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_DOWN && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_DOWN)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_DOWN && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_DOWN)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_LEFT && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_LEFT)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_LEFT && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_LEFT)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_RIGHT && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_RIGHT)
                    }
                    event.button == GamepadManager.GamepadButton.ARROW_RIGHT && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_RIGHT)
                    }
                    event.button == GamepadManager.GamepadButton.BUTTON_A && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_ENTER)
                    }
                    event.button == GamepadManager.GamepadButton.BUTTON_A && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_ENTER)
                    }
                    event.button == GamepadManager.GamepadButton.BUTTON_B && event.direction == GamepadManager.ButtonPressDirection.DOWN -> {
                        robot.keyPress(KeyEvent.VK_ESCAPE)
                    }
                    event.button == GamepadManager.GamepadButton.BUTTON_B && event.direction == GamepadManager.ButtonPressDirection.UP -> {
                        robot.keyRelease(KeyEvent.VK_ESCAPE)
                    }
                }
            }
            .launchIn(this)
    }
}