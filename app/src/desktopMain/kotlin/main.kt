import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arttttt.hendheldclient.arch.context.defaultAppComponentContext
import com.arttttt.hendheldclient.components.root.RootComponent
import com.arttttt.hendheldclient.components.root.RootComponentImpl
import com.arttttt.hendheldclient.di.mviModule
import com.arttttt.hendheldclient.di.repositoryModule
import com.arttttt.hendheldclient.di.storeModule
import com.arttttt.hendheldclient.ui.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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
import org.koin.core.context.startKoin

enum class GamepadButton {
    ARROW_UP,
    ARROW_DOWN,
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

/**
 * todo: move gamepad handling to a separated class
 */
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@ExperimentalDecomposeApi
fun main() {
    startKoin {
        modules(
            mviModule,
            storeModule,
            repositoryModule,
        )
    }

    val lifecycle = LifecycleRegistry()

    val root: RootComponent = RootComponentImpl(
        context = defaultAppComponentContext(
            context = DefaultComponentContext(lifecycle),
            parentScopeID = null,
        )
    )

    val gamepadDispatcher = newSingleThreadContext("GamepadThread")
    val scope = CoroutineScope(SupervisorJob())

    val gamepadEventsFlow = MutableSharedFlow<GamepadKeyEvent>()

    scope.launch(gamepadDispatcher) {
        val controller = ControllerEnvironment
            .getDefaultEnvironment()
            .controllers
            .find { controller ->
                controller.type == Controller.Type.STICK
            }

        controller ?: return@launch

        getGamepadKeyEvents(controller)
            .distinctUntilChanged()
            .onEach(gamepadEventsFlow::emit)
            .launchIn(this)
    }

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = {
                scope.coroutineContext.cancel()
                exitApplication()
            },
            state = windowState,
            title = "Handheld client"
        ) {
            MaterialTheme {
                RootContent(
                    component = root,
                    gamepadEventsFlow = gamepadEventsFlow,
                )
            }
        }
    }
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
