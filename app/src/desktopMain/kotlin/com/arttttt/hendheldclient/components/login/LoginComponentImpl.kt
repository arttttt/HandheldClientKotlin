package com.arttttt.hendheldclient.components.login

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.domain.store.connection.ConnectionStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn

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
                state.token != null -> LoginComponent.UiState.Content(
                    token = state.token.token,
                    port = state.port.port.toString(),
                )
                else -> null
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, LoginComponent.UiState.Progress)

    override fun onContinueClicked() {
        openNextScreen.invoke()
    }
}