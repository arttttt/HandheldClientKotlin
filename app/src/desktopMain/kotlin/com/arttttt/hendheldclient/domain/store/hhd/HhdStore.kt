package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Store
 
 interface HhdStore : Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> {
 
     data class State(
         val isInProgress: Boolean,
     )
 
     sealed class Action {

         data object LoadSettings : Action()
     }
 
     sealed class Intent
 
     sealed class Message {

         data object ProgressStarted : Message()
         data object ProgressFinished : Message()
     }
 
     sealed class Label
 }