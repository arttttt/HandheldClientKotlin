package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField2
import com.arttttt.hendheldclient.domain.entity.settings.SettingFieldRoot
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.Queue

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
        fields: Map<String, SettingFieldRoot>,
        keys: Queue<String>,
    ): SettingField2<*>? {
        val key = keys.poll()
        val field: SettingFieldRoot = fields[key] ?: return null

        return findSettingField2(
            sections = field.items,
            keys = keys,
        )
    }

    private fun findSettingField2(
        sections: Map<String, SettingField2<*>>,
        keys: Queue<String>,
    ): SettingField2<*>? {
        var currentField: SettingField2<*>? = null
        var key = keys.poll()

        while (key != null) {
            currentField = sections[key] ?: break

            when (currentField) {
                is SettingField2.SectionField -> {
                    currentField = findSettingField2(
                        sections = currentField.value,
                        keys = keys,
                    )
                }
                is SettingField2.Mode -> {
                    currentField = when (val mode = currentField.mode) {
                        is SettingField2.SectionField -> {
                            /**
                             * hack
                             *
                             * todo: fix it later
                             */
                            keys.poll()

                            findSettingField2(
                                sections = mode.value,
                                keys = keys,
                            )
                        }
                        else -> mode
                    }
                }
                else -> {}
            }

            key = keys.poll()
        }

        return currentField
    }

    private fun FieldKey.toQueue(): Queue<String> {
        return LinkedList(
            buildList<String?> {
                var currentKey: FieldKey? = this@toQueue

                while (currentKey != null) {
                    this += currentKey.key

                    currentKey = currentKey.parent
                }
            }.reversed()
        )
    }
}