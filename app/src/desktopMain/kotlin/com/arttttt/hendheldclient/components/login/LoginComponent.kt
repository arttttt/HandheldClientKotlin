package com.arttttt.hendheldclient.components.login

import com.arttttt.hendheldclient.arch.DecomposeComponent
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent : DecomposeComponent {

    sealed class UiState {

        data class Content(
            val token: String
        ) : UiState()

        data object Progress : UiState()
    }

    val states: StateFlow<UiState>

    fun onContinueClicked()
}