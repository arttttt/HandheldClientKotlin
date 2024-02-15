package com.arttttt.hendheldclient.components.hhd

import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.components.hhd.di.hhdComponentModule
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore

class HhdComponentImpl(
    context: AppComponentContext,
) : HhdComponent,
    AppComponentContext by context {

    private val koinScope = koinScope(
        hhdComponentModule
    )

    private val hhdStore: HhdStore by koinScope.inject()

    init {
        hhdStore
    }
}