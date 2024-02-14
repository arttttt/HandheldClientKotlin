package com.arttttt.hendheldclient.di

import com.arttttt.hendheldclient.domain.store.TokenStore
import com.arttttt.hendheldclient.domain.store.TokenStoreFactory
import org.koin.dsl.module

val storeModule = module {
    single<TokenStore> {
        TokenStoreFactory(
            storeFactory = get(),
            tokenRepository = get()
        ).create()
    }
}