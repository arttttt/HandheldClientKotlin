package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arttttt.hendheldclient.domain.entity.settings.SettingsSection

interface HhdStore : Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> {
 
     data class State(
         val isInProgress: Boolean,
         val sections: Map<String, SettingsSection>,
         val pendingChanges: Map<String, Map<String, Any?>>,
     )
 
     sealed class Action {

         data object LoadSettings : Action()
     }
 
     sealed class Intent {
         data class SetValue(
             val parent: String,
             val id: String,
             val value: Any,
         ) : Intent()

         data class RemoveOverride(
             val parent: String,
             val id: String
         ) : Intent()
     }
 
     sealed class Message {

         data object ProgressStarted : Message()
         data object ProgressFinished : Message()

         data class SectionsRetrieved(
             val sections: Map<String, SettingsSection>,
         ) : Message()

         data class PendingChangesUpdated(
             val pendingChanges: Map<String, Map<String, Any?>>,
         ) : Message()
     }
 
     sealed class Label
 }