package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionStoreExecutor(
    private val tokenRepository: TokenRepository,
) : CoroutineExecutor<ConnectionStore.Intent, ConnectionStore.Action, ConnectionStore.State, ConnectionStore.Message, ConnectionStore.Label>() {

    override fun executeAction(action: ConnectionStore.Action) {
        when (action) {
            is ConnectionStore.Action.RetrieveToken -> retrieveToken()
        }
    }

    override fun executeIntent(intent: ConnectionStore.Intent) {
        super.executeIntent(intent)
    }

    private fun retrieveToken() {
        scope.launch {
            dispatch(ConnectionStore.Message.ProgressStarted)

            val token = withContext(Dispatchers.IO) {
                tokenRepository.getHhdToken()
            }

            dispatch(
                ConnectionStore.Message.TokenRetrieved(
                    token = token,
                )
            )

            dispatch(ConnectionStore.Message.ProgressFinished)
        }
    }
}