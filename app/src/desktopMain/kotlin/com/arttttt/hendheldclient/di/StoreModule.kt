package com.arttttt.hendheldclient.di

import com.arttttt.hendheldclient.domain.store.connection.ConnectionStore
import com.arttttt.hendheldclient.domain.store.connection.ConnectionStoreFactory
import org.koin.dsl.module

val storeModule = module {
    single<ConnectionStore> {
        ConnectionStoreFactory(
            storeFactory = get(),
            tokenRepository = get()
        ).create()
    }
}