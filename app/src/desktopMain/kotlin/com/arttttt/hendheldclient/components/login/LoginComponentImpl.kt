package com.arttttt.hendheldclient.components.login

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.domain.store.TokenStore
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

    private val tokenStore: TokenStore by koinScope.inject()

    override val states: StateFlow<LoginComponent.UiState> = tokenStore
        .states
        .mapNotNull { state ->
            when {
                state.isInProgress -> LoginComponent.UiState.Progress
                state.token != null -> LoginComponent.UiState.Content(state.token.token)
                else -> null
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, LoginComponent.UiState.Progress)

    override fun onContinueClicked() {
        openNextScreen.invoke()
    }
}