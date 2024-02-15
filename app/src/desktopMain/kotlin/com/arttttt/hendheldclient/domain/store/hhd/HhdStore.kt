package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Store
 
 interface HhdStore : Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> {
 
     data class State(
         val value: Int = 0
     )
 
     sealed class Action
 
     sealed class Intent
 
     sealed class Message
 
     sealed class Label
 }