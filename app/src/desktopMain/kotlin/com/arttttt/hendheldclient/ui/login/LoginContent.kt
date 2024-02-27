package com.arttttt.hendheldclient.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.components.login.LoginComponent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginContent(component: LoginComponent) {
    val state by component.states.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    SnackbarHost(snackbarHostState)

    when (val castedState = state) {
        is LoginComponent.UiState.Progress -> ProgressContent()
        is LoginComponent.UiState.Content -> TokenContent(
            token = castedState.token,
            port = castedState.port,
            onContinueClicked = component::onContinueClicked,
            onPortChanged = component::onPortChanged,
            onTokenChanged = component::onTokenChanged,
        )
    }

    LaunchedEffect(component) {
        component
            .commands
            .onEach { command ->
                when (command) {
                    is LoginComponent.Command.ShowMessage -> {
                       snackbarHostState.showSnackbar(command.message)
                    }
                }
            }
            .launchIn(this)
    }
}

@Composable
private fun ProgressContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TokenContent(
    token: String,
    port: String,
    onTokenChanged: (String) -> Unit,
    onPortChanged: (String) -> Unit,
    onContinueClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = token,
            onValueChange = onTokenChanged,
            label = {
                Text("token")
            }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = port,
            onValueChange = onPortChanged,
            label = {
                Text("port")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            )
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onContinueClicked,
        ) {
            Text("continue")
        }
    }
}