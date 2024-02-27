package com.arttttt.hendheldclient.components.login

import com.arttttt.hendheldclient.arch.DecomposeComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent : DecomposeComponent {

    sealed interface UiState {

        data class Content(
            val token: String,
            val port: String,
        ) : UiState

        data object Progress : UiState
    }

    sealed interface Command {

        data class ShowMessage(
            val message: String
        ) : Command
    }

    val states: StateFlow<UiState>

    val commands: Flow<Command>

    fun onContinueClicked()

    fun onPortChanged(port: String)

    fun onTokenChanged(token: String)
}