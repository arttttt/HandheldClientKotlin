package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Reducer
 
 object HhdStoreReducer : Reducer<HhdStore.State, HhdStore.Message> {
 
     override fun HhdStore.State.reduce(msg: HhdStore.Message): HhdStore.State {
         return this
     }
 }