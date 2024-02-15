package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.hendheldclient.domain.entity.HhdPort
import com.arttttt.hendheldclient.domain.repository.TokenRepository

class ConnectionStoreFactory(
    private val storeFactory: StoreFactory,
    private val tokenRepository: TokenRepository,
) {

    fun create(): ConnectionStore {
        return object : ConnectionStore, Store<ConnectionStore.Intent, ConnectionStore.State, ConnectionStore.Label> by storeFactory.create(
            name = ConnectionStore::class.qualifiedName,
            initialState = ConnectionStore.State(
                token = null,
                isInProgress = false,
                port = HhdPort(
                    port = 5335,
                ),
            ),
            bootstrapper = SimpleBootstrapper(
                ConnectionStore.Action.RetrieveToken
            ),
            executorFactory = {
                ConnectionStoreExecutor(
                    tokenRepository = tokenRepository,
                )
            },
            reducer = ConnectionStoreReducer,
        ) {}
    }
}