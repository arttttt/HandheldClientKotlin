package com.arttttt.hendheldclient.domain.store.connection

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.entity.HhdPort

interface ConnectionStore : Store<ConnectionStore.Intent, ConnectionStore.State, ConnectionStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val token: HhdAuthToken?,
        val port: HhdPort?,
    )

    sealed class Action {

        data object RetrieveConnectionInfo : Action()
    }

    sealed class Intent

    sealed class Message {

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()

        data class ConnectionInfoRetrieved(
            val token: HhdAuthToken,
            val port: HhdPort,
        ) : Message()
    }

    sealed class Label
}