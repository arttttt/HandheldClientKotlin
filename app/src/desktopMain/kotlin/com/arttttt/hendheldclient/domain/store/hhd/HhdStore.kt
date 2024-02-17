package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arttttt.hendheldclient.domain.entity.settings.SettingsSection

interface HhdStore : Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> {
 
     data class State(
         val isInProgress: Boolean,
         val sections: Map<String, SettingsSection>,
     )
 
     sealed class Action {

         data object LoadSettings : Action()
     }
 
     sealed class Intent
 
     sealed class Message {

         data object ProgressStarted : Message()
         data object ProgressFinished : Message()

         data class SectionsRetrieved(
             val sections: Map<String, SettingsSection>,
         ) : Message()
     }
 
     sealed class Label
 }