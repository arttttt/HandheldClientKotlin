package com.arttttt.hendheldclient.components.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.components.hhd.di.hhdComponentModule
import com.arttttt.hendheldclient.domain.entity.settings.SettingField
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    override val states: StateFlow<HhdComponent.UiState> = hhdStore
        .states
        .map { state ->
            HhdComponent.UiState(
                items = state.sections.map { (_, section) ->
                    ContainerListItem(
                        title = section.title,
                        children = section.fields.map { (_, field) ->
                            when (field) {
                                is SettingField.DisplayField -> TextListItem(
                                    title = field.title,
                                    value = field.value,
                                )
                                is SettingField.ActionField -> ActionListItem(
                                    title = field.title,
                                    isEnabled = field.value == true,
                                )
                                is SettingField.BooleanField -> BooleanListItem(
                                    title = field.title,
                                    isChecked = field.value,
                                )
                                is SettingField.IntField -> IntListItem(
                                    title = field.title,
                                    value = field.value.toString(),
                                )
                            }
                        },
                    )
                }
            )
        }
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5000),
            HhdComponent.UiState(
                items = emptyList(),
            )
        )

    init {
        hhdStore
    }
}