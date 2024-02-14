package com.arttttt.hendheldclient.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module

val mviModule = module {
    single<StoreFactory> {
        LoggingStoreFactory(
            delegate = DefaultStoreFactory()
        )
    }
}