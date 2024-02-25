package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField2
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
        }
    }

    private fun setValue2(
        key: FieldKey,
        value: Any,
    ) {
        val reversedKeys = key.toReversedList()

        val field = findSettingField(
            state = state(),
            keys = reversedKeys,
        )

        when (field) {
            is SettingField2.IntInputField -> {
                dispatch(
                    HhdStore.Message.PendingChangesUpdated2(
                        pendingChanges = state()
                            .pendingChanges2
                            .toMutableMap()
                            .apply {
                                put(
                                    key = key,
                                    value = value,
                                )
                            }
                            .toMap()
                    )
                )
            }

            is SettingField2.BooleanField -> {
                dispatch(
                    HhdStore.Message.PendingChangesUpdated2(
                        pendingChanges = state()
                            .pendingChanges2
                            .toMutableMap()
                            .apply {
                                put(
                                    key = key,
                                    value = value
                                )
                            }
                            .toMap()
                    )
                )
            }

            else -> {}
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
                    sections = withContext(Dispatchers.IO) {
                        hhdRepository.getSettings()
                    }
                )
            )

            dispatch(HhdStore.Message.ProgressFinished)
        }
    }

    private fun findSettingField(state: HhdStore.State, keys: List<String>): SettingField2<*>? {
        var currentField: SettingField2<*>? = null
        var sections = state.sections

        for (key in keys) {
            currentField = sections[key] ?: return null

            if (currentField is SettingField2.SectionField) {
                sections = currentField.value
            } else if (currentField is SettingField2.Mode) {
                val modeField = currentField.mode
                if (modeField is SettingField2.SectionField) {
                    sections = modeField.value
                } else {
                    return modeField
                }
            } else {
                if (keys.indexOf(key) < keys.size - 1) return null
            }
        }
        return currentField
    }

    private fun FieldKey.toReversedList(): List<String> {
        return buildList {
            var currentKey: FieldKey? = this@toReversedList

            while (currentKey != null) {
                if (currentKey.parent == null) break

                this += currentKey.key

                currentKey = currentKey.parent
            }
        }.reversed()
    }
}