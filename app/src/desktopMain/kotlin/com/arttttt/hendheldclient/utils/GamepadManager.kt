package com.arttttt.hendheldclient.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import net.java.games.input.Component
import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Event

/**
 * todo: implement attach/detach gamepad logic
 */
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
object GamepadManager {

    enum class GamepadButton {
        ARROW_UP,
        ARROW_DOWN,
        ARROW_LEFT,
        ARROW_RIGHT,
        BUTTON_A,
        BUTTON_B;
    }

    enum class ButtonPressDirection {
        DOWN,
        UP;
    }

    data class GamepadKeyEvent(
        val button: GamepadButton,
        val direction: ButtonPressDirection,
    )

    private val gamepadDispatcher = newSingleThreadContext("GamepadThread")

    private val scope = CoroutineScope(SupervisorJob())

    private val gamepadEventsFlow = MutableSharedFlow<GamepadKeyEvent>()

    val gamepadEvents = gamepadEventsFlow.asSharedFlow()

    fun attach() {
        scope.launch(gamepadDispatcher) {
            val controller = ControllerEnvironment
                .getDefaultEnvironment()
                .controllers
                .find { controller ->
                    controller.type == Controller.Type.STICK// || controller.type == Controller.Type.GAMEPAD
                }

            controller ?: return@launch

            getGamepadKeyEvents(controller)
                .distinctUntilChanged()
                .onEach(gamepadEventsFlow::emit)
                .launchIn(this)
        }
    }

    fun detach() {
        scope.coroutineContext.cancel()
    }

    private fun getGamepadKeyEvents(
        controller: Controller
    ): Flow<GamepadKeyEvent> {
        return flow {
            var currentPov = 0f

            while (true) {
                val event = Event()

                controller.poll()

                val queue = controller.eventQueue

                while (queue.getNextEvent(event)) {
                    when {
                        event.component.identifier == Component.Identifier.Button.A -> {
                            emit(
                                GamepadKeyEvent(
                                    button = GamepadButton.BUTTON_A,
                                    direction = if (event.value == 1f) {
                                        ButtonPressDirection.DOWN
                                    } else {
                                        ButtonPressDirection.UP
                                    }
                                )
                            )
                        }
                        event.component.identifier == Component.Identifier.Button.B -> {
                            emit(
                                GamepadKeyEvent(
                                    button = GamepadButton.BUTTON_B,
                                    direction = if (event.value == 1f) {
                                        ButtonPressDirection.DOWN
                                    } else {
                                        ButtonPressDirection.UP
                                    }
                                )
                            )
                        }
                        event.component.identifier == Component.Identifier.Axis.POV -> {
                            val prevPov = currentPov
                            currentPov = event.value

                            val lastCorrectPov = when {
                                currentPov == 0f -> prevPov
                                else -> currentPov
                            }

                            val button = when (lastCorrectPov) {
                                Component.POV.UP -> GamepadButton.ARROW_UP
                                Component.POV.DOWN -> GamepadButton.ARROW_DOWN
                                Component.POV.LEFT -> GamepadButton.ARROW_LEFT
                                Component.POV.RIGHT -> GamepadButton.ARROW_RIGHT
                                else -> null
                            }

                            button ?: continue

                            val direction = when {
                                currentPov == 0f -> ButtonPressDirection.UP
                                else -> ButtonPressDirection.DOWN
                            }

                            emit(
                                GamepadKeyEvent(
                                    button = button,
                                    direction = direction,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}