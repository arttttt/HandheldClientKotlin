package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField3
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import com.arttttt.hendheldclient.utils.filterValuesNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Stack

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
            is HhdStore.Intent.SetValue2 -> {
                setValue2(
                    key = intent.key,
                    value = intent.value,
                )
            }
            is HhdStore.Intent.RemoveOverride2 -> {
                removeOverrides2(
                    key = intent.key,
                )
            }
            is HhdStore.Intent.ApplyOverrides -> applyOverrides()
            is HhdStore.Intent.ClearOverrides -> clearOverrides()
        }
    }

    private fun applyOverrides() {
        scope.launch {
            withContext(Dispatchers.IO) {
                hhdRepository.applyOverrides(
                    overrides = state().pendingChanges2.filterValuesNotNull(),
                )
            }

            dispatch(
                HhdStore.Message.PendingChangesUpdated2(
                    pendingChanges = emptyMap(),
                )
            )

            executeAction(HhdStore.Action.LoadSettings)
        }
    }
    private fun clearOverrides() {
        dispatch(
            HhdStore.Message.PendingChangesUpdated2(
                pendingChanges = emptyMap(),
            )
        )
    }

    private fun setValue2(
        key: FieldKey,
        value: Any,
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                findSettingField(
                    sections = state().fields,
                    keys = key.toStack(),
                )
            }

            val updatedValue = when (result) {
                is SettingField3.Fields.IntInputField -> {
                    value.takeIf { it != "" }?.toString()?.filter { it.isDigit() }?.toIntOrNull()
                }
                else -> value
            }

            dispatch(
                HhdStore.Message.PendingChangesUpdated2(
                    pendingChanges = withContext(Dispatchers.IO) {
                        state()
                            .pendingChanges2
                            .toMutableMap()
                            .apply {
                                put(
                                    key = key,
                                    value = updatedValue,
                                )
                            }
                            .toMap()
                    }
                )
            )
        }
    }

    private fun removeOverrides2(
        key: FieldKey
    ) {
        dispatch(
            HhdStore.Message.PendingChangesUpdated2(
                pendingChanges = state()
                    .pendingChanges2
                    .toMutableMap()
                    .apply {
                        remove(key)
                    }
                    .toMap()
            )
        )
    }

    private fun loadSettings() {
        scope.launch {
            dispatch(HhdStore.Message.ProgressStarted)

            dispatch(
                HhdStore.Message.SectionsRetrieved(
                    fields = withContext(Dispatchers.IO) {
                        hhdRepository.getSettings()
                    }
                )
            )

            dispatch(HhdStore.Message.ProgressFinished)
        }
    }

    private fun findSettingField(
        sections: Map<String, SettingField3>,
        keys: Stack<String>,
    ): SettingField3? {
        var currentField: SettingField3? = null

        while (keys.isNotEmpty()) {
            val key = keys.pop()

            currentField = sections[key] ?: break

            when (currentField) {
                is SettingField3.RootField -> {
                    currentField = findSettingField(
                        sections = currentField.items,
                        keys = keys,
                    )
                }
                is SettingField3.Fields.SectionField -> {
                    currentField = findSettingField(
                        sections = currentField.value,
                        keys = keys,
                    )
                }
                is SettingField3.Fields.Mode -> {
                    currentField = when {
                        keys.isEmpty() -> currentField
                        else -> when (val mode = currentField.mode) {
                            is SettingField3.Fields.SectionField -> {
                                /**
                                 * drop the current key
                                 */
                                keys.pop()

                                findSettingField(
                                    sections = mode.value,
                                    keys = keys,
                                )
                            }
                            else -> mode
                        }
                    }
                }
                else -> {}
            }
        }

        return currentField
    }

    private fun FieldKey.toStack(): Stack<String> {
        val stack = Stack<String>()
        var currentKey: FieldKey? = this

        while (currentKey != null) {
            stack.push(currentKey.key)

            currentKey = currentKey.parent
        }

        return stack
    }
}