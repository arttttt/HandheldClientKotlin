package com.arttttt.hendheldclient.arch

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import org.koin.core.component.getScopeId
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.core.scope.ScopeID
import org.koin.mp.KoinPlatform

inline fun <reified T> T.koinScope(
    vararg modules: Module,
    scopeID: ScopeID = getScopeId(),
    qualifier: Qualifier = qualifier<T>(),
) : Scope where T : DecomposeComponent,
                T : AppComponentContext
{
    val scope = KoinPlatform.getKoin().createScope(
        scopeId = scopeID,
        qualifier = qualifier,
        source = null,
    )

    val modulesList = modules.toList()

    parentScopeID
        ?.let(scope::getScope)
        ?.let { parentScope ->
            scope.linkTo(parentScope)
        }

    KoinPlatform.getKoin().loadModules(
        modulesList,
    )

    scope.registerCallback(
        object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                KoinPlatform.getKoin().unloadModules(
                    modulesList
                )
            }
        }
    )

    lifecycle.doOnDestroy {
        scope.close()
    }

    return scope
}