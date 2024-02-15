package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
 import com.arttttt.hendheldclient.domain.repository.HhdRepository

/**
  * todo: provide dispatchers provider
  */
 class HhdStoreExecutor(
     private val hhdRepository: HhdRepository,
 ) : CoroutineExecutor<HhdStore.Intent, HhdStore.Action, HhdStore.State, HhdStore.Message, HhdStore.Label>() {
 
     override fun executeAction(action: HhdStore.Action) {
         super.executeAction(action)
     }
 
     override fun executeIntent(intent: HhdStore.Intent) {
         super.executeIntent(intent)
     }
 }