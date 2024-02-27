package com.arttttt.hendheldclient.components.login

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.domain.store.connection.ConnectionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn

/**
 * todo: add more states and handle them properly
 */
class LoginComponentImpl(
    context: AppComponentContext,
    private val openNextScreen: () -> Unit,
) : LoginComponent,
    AppComponentContext by context {

    private val koinScope = koinScope()

    private val connectionStore: ConnectionStore by koinScope.inject()

    override val states: StateFlow<LoginComponent.UiState> = connectionStore
        .states
        .mapNotNull { state ->
            when {
                state.isInProgress -> LoginComponent.UiState.Progress
                state.token != null && state.port != null -> LoginComponent.UiState.Content(
                    token = state.token.token,
                    port = state.port.port.toString(),
                )
                else -> null
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, LoginComponent.UiState.Progress)

    override val commands = MutableSharedFlow<LoginComponent.Command>(extraBufferCapacity = 1)

    override fun onContinueClicked() {
        openNextScreen.invoke()
    }

    override fun onPortChanged(port: String) {
        commands.tryEmit(
            LoginComponent.Command.ShowMessage(
                message = "not implemented yet"
            )
        )
    }

    override fun onTokenChanged(token: String) {
        commands.tryEmit(
            LoginComponent.Command.ShowMessage(
                message = "not implemented yet"
            )
        )
    }
}