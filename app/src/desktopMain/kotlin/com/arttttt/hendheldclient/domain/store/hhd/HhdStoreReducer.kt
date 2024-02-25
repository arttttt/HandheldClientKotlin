package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Reducer
 
 object HhdStoreReducer : Reducer<HhdStore.State, HhdStore.Message> {
 
     override fun HhdStore.State.reduce(msg: HhdStore.Message): HhdStore.State {
         return when (msg) {
             is HhdStore.Message.ProgressStarted -> copy(
                 isInProgress = true,
             )
             is HhdStore.Message.ProgressFinished -> copy(
                 isInProgress = false,
             )
             is HhdStore.Message.SectionsRetrieved -> copy(
                 sections = msg.sections,
             )
             is HhdStore.Message.PendingChangesUpdated2 -> copy(
                 pendingChanges2 = msg.pendingChanges,
             )
         }
     }
 }