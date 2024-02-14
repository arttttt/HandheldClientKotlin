package com.arttttt.hendheldclient.domain.store

import com.arkivanov.mvikotlin.core.store.Reducer

object TokenStoreReducer : Reducer<TokenStore.State, TokenStore.Message> {

    override fun TokenStore.State.reduce(msg: TokenStore.Message): TokenStore.State {
        return when (msg) {
            is TokenStore.Message.TokenRetrieved -> copy(
                token = msg.token,
            )
        }
    }
}