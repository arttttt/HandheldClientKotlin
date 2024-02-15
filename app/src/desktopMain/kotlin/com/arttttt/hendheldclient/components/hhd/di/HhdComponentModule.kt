package com.arttttt.hendheldclient.components.hhd.di

import com.arttttt.hendheldclient.data.repository.HhdRepositoryImpl
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import com.arttttt.hendheldclient.domain.store.hhd.HhdStoreFactory
import org.koin.dsl.module

val hhdComponentModule = module {
    single<HhdRepository> {
        HhdRepositoryImpl(
            api = get()
        )
    }

    single {
        HhdStoreFactory(
            storeFactory = get(),
            hhdRepository = get(),
        ).create()
    }
}