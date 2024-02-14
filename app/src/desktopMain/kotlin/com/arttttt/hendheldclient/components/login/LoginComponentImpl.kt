package com.arttttt.hendheldclient.components.login

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.domain.store.TokenStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LoginComponentImpl(
    context: AppComponentContext,
    private val openNextScreen: () -> Unit,
) : LoginComponent,
    AppComponentContext by context {

    private val koinScope = koinScope()

    private val tokenStore: TokenStore by koinScope.inject()

    override val states: StateFlow<LoginComponent.UiState> = tokenStore
        .states
        .map { state ->
            if (state.token == null) {
                LoginComponent.UiState.Progress
            } else
                LoginComponent.UiState.Content(
                    token = state.token.token
                )
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, LoginComponent.UiState.Progress)

    override fun onContinueClicked() {
        openNextScreen.invoke()
    }
}