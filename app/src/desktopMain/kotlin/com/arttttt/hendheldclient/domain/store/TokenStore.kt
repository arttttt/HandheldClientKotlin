package com.arttttt.hendheldclient.domain.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.hendheldclient.domain.entity.HhdAuthToken

interface TokenStore : Store<TokenStore.Intent, TokenStore.State, TokenStore.Label> {

    data class State(
        val token: HhdAuthToken?
    )

    sealed class Action {

        data object RetrieveToken : Action()
    }

    sealed class Intent

    sealed class Message {

        data class TokenRetrieved(
            val token: HhdAuthToken
        ) : Message()
    }

    sealed class Label
}