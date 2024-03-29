package com.arttttt.hendheldclient.arch.context

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.scope.ScopeID

interface AppComponentContext : ComponentContext {

    val parentScopeID: ScopeID?
    val coroutineScope: CoroutineScope
}