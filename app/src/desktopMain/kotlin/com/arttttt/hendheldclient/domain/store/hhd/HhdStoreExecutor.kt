package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.entity.settings.SettingField
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * todo: provide dispatchers provider
 */
class HhdStoreExecutor(
    private val hhdRepository: HhdRepository,
) : CoroutineExecutor<HhdStore.Intent, HhdStore.Action, HhdStore.State, HhdStore.Message, HhdStore.Label>(
    mainContext = Dispatchers.Main.immediate
) {

    override fun executeAction(action: HhdStore.Action) {
        when (action) {
            is HhdStore.Action.LoadSettings -> loadSettings()
        }
    }

    override fun executeIntent(intent: HhdStore.Intent) {
        when (intent) {
            is HhdStore.Intent.SetValue -> {
                setValue(
                    parent = intent.parent,
                    id = intent.id,
                    value = intent.value,
                )
            }
            is HhdStore.Intent.RemoveOverride -> {
                removeOverride(
                    parent = intent.parent,
                    id = intent.id,
                )
            }
        }
    }

    private fun removeOverride(
        parent: String,
        id: String
    ) {
        dispatch(
            HhdStore.Message.PendingChangesUpdated(
                pendingChanges = state()
                    .pendingChanges
                    .toMutableMap()
                    .apply {
                        put(
                            parent,
                            getValue(parent)
                                .toMutableMap()
                                .apply {
                                    remove(id)
                                }
                                .toMap()
                        )
                    }
                    .toMap()
            )
        )
    }

    private fun setValue(
        parent: String,
        id: String,
        value: Any,
    ) {
        val section = state().sections.getValue(parent)
        val field = section.fields.getValue(id)

        when (field) {
            is SettingField.IntInputField -> {
                dispatch(
                    HhdStore.Message.PendingChangesUpdated(
                        pendingChanges = state()
                            .pendingChanges
                            .toMutableMap()
                            .apply {
                                put(
                                    section.id,
                                    getOrDefault(field.id, mapOf())
                                        .toMutableMap()
                                        .apply {
                                            put(
                                                field.id,
                                                value,
                                            )
                                        }
                                        .toMap()
                                )
                            }
                            .toMap()
                    )
                )
            }
            is SettingField.BooleanField -> {
                dispatch(
                    HhdStore.Message.PendingChangesUpdated(
                        pendingChanges = state()
                            .pendingChanges
                            .toMutableMap()
                            .apply {
                                put(
                                    section.id,
                                    getOrDefault(field.id, mapOf())
                                        .toMutableMap()
                                        .apply {
                                            put(
                                                field.id,
                                                value,
                                            )
                                        }
                                        .toMap()
                                )
                            }
                            .toMap()
                    )
                )
            }
            is SettingField.ActionField -> null
            is SettingField.DisplayField -> null
        }
    }

    private fun loadSettings() {
        scope.launch {
            dispatch(HhdStore.Message.ProgressStarted)

            dispatch(
                HhdStore.Message.SectionsRetrieved(
                    sections = withContext(Dispatchers.IO) {
                        hhdRepository.getSettings()
                    }
                )
            )

            dispatch(HhdStore.Message.ProgressFinished)
        }
    }
}