package com.arttttt.hendheldclient.domain.store.hhd
 
 import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arkivanov.mvikotlin.core.store.StoreFactory
 import com.arttttt.hendheldclient.domain.repository.HhdRepository

class HhdStoreFactory(
     private val storeFactory: StoreFactory,
     private val hhdRepository: HhdRepository
 ) {
 
     fun create(): HhdStore {
         val name = HhdStore::class.qualifiedName
         val initialState = HhdStore.State(
             isInProgress = false,
             sections = emptyMap(),
             pendingChanges = emptyMap(),
         )
 
         return object : HhdStore,
             Store<HhdStore.Intent, HhdStore.State, HhdStore.Label> by storeFactory.create(
                 name = name,
                 initialState = initialState,
                 bootstrapper = SimpleBootstrapper(
                     HhdStore.Action.LoadSettings,
                 ),
                 executorFactory = {
                     HhdStoreExecutor(
                         hhdRepository = hhdRepository,
                     )
                 },
                 reducer = HhdStoreReducer,
             ) {}
     }
 }