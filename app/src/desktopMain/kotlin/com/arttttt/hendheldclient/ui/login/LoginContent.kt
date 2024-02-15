package com.arttttt.hendheldclient.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.hendheldclient.components.login.LoginComponent

@Composable
fun LoginContent(component: LoginComponent) {
    val state by component.states.collectAsState()

    when (val castedState = state) {
        is LoginComponent.UiState.Progress -> ProgressContent()
        is LoginComponent.UiState.Content -> TokenContent(
            token = castedState.token,
            port = castedState.port,
            onContinueClicked = component::onContinueClicked,
        )
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
    onContinueClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("current token: $token")

        Spacer(Modifier.height(16.dp))

        Text("current port: $port")

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onContinueClicked,
        ) {
            Text("continue")
        }
    }
}