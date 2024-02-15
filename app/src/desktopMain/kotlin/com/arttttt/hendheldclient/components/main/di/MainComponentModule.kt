package com.arttttt.hendheldclient.components.main.di

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.domain.store.connection.ConnectionStore
import org.koin.dsl.module

val mainComponentModule = module {
    single {
        val connectionStore = get<ConnectionStore>()

        /**
         * everything must be retrieved if we are here
         */
        HhdApi(
            token = connectionStore.state.token!!,
            port = connectionStore.state.port!!,
        )
    }
}