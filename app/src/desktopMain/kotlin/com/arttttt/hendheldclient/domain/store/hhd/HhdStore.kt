package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
 import com.arttttt.hendheldclient.domain.entity.settings.SettingField3

interface HhdStore : Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> {
 
     data class State(
         val isInProgress: Boolean,
         val fields: Map<String, SettingField3>,
         val pendingChanges2: Map<FieldKey, Any?>,
     )
 
     sealed class Action {

         data object LoadSettings : Action()
     }
 
     sealed class Intent {

         data class SetValue2(
             val key: FieldKey,
             val value: Any,
         ) : Intent()

         data class RemoveOverride2(
             val key: FieldKey,
         ) : Intent()

         data object ClearOverrides : Intent()

         data object ApplyOverrides : Intent()
     }
 
     sealed class Message {

         data object ProgressStarted : Message()
         data object ProgressFinished : Message()

         data class SectionsRetrieved(
             val fields: Map<String, SettingField3>,
         ) : Message()

         data class PendingChangesUpdated2(
             val pendingChanges: Map<FieldKey, Any?>,
         ) : Message()
     }
 
     sealed class Label
 }