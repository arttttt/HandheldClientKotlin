package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.repository.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionStoreExecutor(
    private val connectionRepository: ConnectionRepository,
) : CoroutineExecutor<ConnectionStore.Intent, ConnectionStore.Action, ConnectionStore.State, ConnectionStore.Message, ConnectionStore.Label>() {

    override fun executeAction(action: ConnectionStore.Action) {
        when (action) {
            is ConnectionStore.Action.RetrieveConnectionInfo -> retrieveToken()
        }
    }

    override fun executeIntent(intent: ConnectionStore.Intent) {
        super.executeIntent(intent)
    }

    private fun retrieveToken() {
        scope.launch {
            dispatch(ConnectionStore.Message.ProgressStarted)

            val token = withContext(Dispatchers.IO) {
                connectionRepository.getHhdToken()
            }

            val port = connectionRepository.getDefaultPort()

            dispatch(
                ConnectionStore.Message.ConnectionInfoRetrieved(
                    token = token,
                    port = port,
                )
            )

            dispatch(ConnectionStore.Message.ProgressFinished)
        }
    }
}