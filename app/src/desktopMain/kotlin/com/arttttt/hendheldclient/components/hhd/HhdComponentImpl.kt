package com.arttttt.hendheldclient.components.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.components.hhd.di.hhdComponentModule
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore
import com.arttttt.hendheldclient.ui.hhd.HhdTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HhdComponentImpl(
    context: AppComponentContext,
) : HhdComponent,
    AppComponentContext by context {

    private val koinScope = koinScope(
        hhdComponentModule
    )

    private val hhdStore: HhdStore by koinScope.inject()

    private val transformer = HhdTransformer()

    override val states: StateFlow<HhdComponent.UiState> = hhdStore
        .states
        .map(transformer::invoke)
        .flowOn(Dispatchers.IO)
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5000),
            HhdComponent.UiState(
                items = emptyList(),
            )
        )

    override fun onValueUpdated(key: FieldKey, value: Any) {
        hhdStore.accept(
            HhdStore.Intent.SetValue2(
                key = key,
                value = value,
            )
        )
    }

    override fun onResetClicked(key: FieldKey) {
        hhdStore.accept(
            HhdStore.Intent.RemoveOverride2(
                key = key,
            )
        )
    }
}