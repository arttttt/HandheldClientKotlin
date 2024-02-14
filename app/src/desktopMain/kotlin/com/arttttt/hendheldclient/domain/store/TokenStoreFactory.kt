package com.arttttt.hendheldclient.domain.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.hendheldclient.domain.repository.TokenRepository

class TokenStoreFactory(
    private val storeFactory: StoreFactory,
    private val tokenRepository: TokenRepository,
) {

    fun create(): TokenStore {
        return object : TokenStore, Store<TokenStore.Intent, TokenStore.State, TokenStore.Label> by storeFactory.create(
            name = TokenStore::class.qualifiedName,
            initialState = TokenStore.State(
                token = null,
                isInProgress = false,
            ),
            bootstrapper = SimpleBootstrapper(
                TokenStore.Action.RetrieveToken
            ),
            executorFactory = {
                TokenStoreExecutor(
                    tokenRepository = tokenRepository,
                )
            },
            reducer = TokenStoreReducer,
        ) {}
    }
}