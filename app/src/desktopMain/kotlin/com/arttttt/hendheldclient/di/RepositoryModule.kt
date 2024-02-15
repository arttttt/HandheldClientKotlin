package com.arttttt.hendheldclient.di

import com.arttttt.hendheldclient.data.repository.ConnectionRepositoryImpl
import com.arttttt.hendheldclient.domain.repository.ConnectionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ConnectionRepository> {
        ConnectionRepositoryImpl()
    }
}