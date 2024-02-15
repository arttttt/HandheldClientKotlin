package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.hendheldclient.domain.repository.ConnectionRepository

class ConnectionStoreFactory(
    private val storeFactory: StoreFactory,
    private val connectionRepository: ConnectionRepository,
) {

    fun create(): ConnectionStore {
        return object : ConnectionStore, Store<ConnectionStore.Intent, ConnectionStore.State, ConnectionStore.Label> by storeFactory.create(
            name = ConnectionStore::class.qualifiedName,
            initialState = ConnectionStore.State(
                token = null,
                isInProgress = false,
                port = null,
            ),
            bootstrapper = SimpleBootstrapper(
                ConnectionStore.Action.RetrieveConnectionInfo
            ),
            executorFactory = {
                ConnectionStoreExecutor(
                    connectionRepository = connectionRepository,
                )
            },
            reducer = ConnectionStoreReducer,
        ) {}
    }
}