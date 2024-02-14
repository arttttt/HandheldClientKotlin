package com.arttttt.hendheldclient.arch.context

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.koin.core.scope.ScopeID

fun defaultAppComponentContext(
    context: ComponentContext,
    parentScopeID: ScopeID?,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
): AppComponentContext {
    val coroutineScope = CoroutineScope(dispatcher + SupervisorJob())

    context.lifecycle.doOnDestroy {
        coroutineScope.coroutineContext.cancelChildren()
    }

    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
        coroutineScope = coroutineScope,
    )
}