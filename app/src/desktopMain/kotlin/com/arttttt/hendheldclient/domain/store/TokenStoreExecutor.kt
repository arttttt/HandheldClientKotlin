package com.arttttt.hendheldclient.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TokenStoreExecutor(
    private val tokenRepository: TokenRepository,
) : CoroutineExecutor<TokenStore.Intent, TokenStore.Action, TokenStore.State, TokenStore.Message, TokenStore.Label>() {

    override fun executeAction(action: TokenStore.Action) {
        when (action) {
            is TokenStore.Action.RetrieveToken -> retrieveToken()
        }
    }

    override fun executeIntent(intent: TokenStore.Intent) {
        super.executeIntent(intent)
    }

    private fun retrieveToken() {
        scope.launch {
            val token = withContext(Dispatchers.IO) {
                tokenRepository.getHhdToken()
            }

            dispatch(
                TokenStore.Message.TokenRetrieved(
                    token = token,
                )
            )
        }
    }
}