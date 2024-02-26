package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
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
}