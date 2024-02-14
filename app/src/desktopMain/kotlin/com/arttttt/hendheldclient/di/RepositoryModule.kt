package com.arttttt.hendheldclient.di

import com.arttttt.hendheldclient.data.TokenRepositoryImpl
import com.arttttt.hendheldclient.domain.repository.TokenRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TokenRepository> {
        TokenRepositoryImpl()
    }
}