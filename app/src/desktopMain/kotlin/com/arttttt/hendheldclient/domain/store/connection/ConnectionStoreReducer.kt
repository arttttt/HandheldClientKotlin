package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.core.store.Reducer

object ConnectionStoreReducer : Reducer<ConnectionStore.State, ConnectionStore.Message> {

    override fun ConnectionStore.State.reduce(msg: ConnectionStore.Message): ConnectionStore.State {
        return when (msg) {
            is ConnectionStore.Message.TokenRetrieved -> copy(
                token = msg.token,
            )
            is ConnectionStore.Message.ProgressStarted -> copy(
                isInProgress = true
            )
            is ConnectionStore.Message.ProgressFinished -> copy(
                isInProgress = false,
            )
        }
    }
}