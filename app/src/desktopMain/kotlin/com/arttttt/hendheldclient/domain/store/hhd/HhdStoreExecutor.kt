package com.arttttt.hendheldclient.domain.store.hhd

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * todo: provide dispatchers provider
 */
class HhdStoreExecutor(
    private val hhdRepository: HhdRepository,
) : CoroutineExecutor<HhdStore.Intent, HhdStore.Action, HhdStore.State, HhdStore.Message, HhdStore.Label>() {

    override fun executeAction(action: HhdStore.Action) {
        when (action) {
            is HhdStore.Action.LoadSettings -> loadSettings()
        }
    }

    override fun executeIntent(intent: HhdStore.Intent) {
        super.executeIntent(intent)
    }

    private fun loadSettings() {
        scope.launch {
            dispatch(HhdStore.Message.ProgressStarted)

            println(
                withContext(Dispatchers.IO) {
                    hhdRepository.getSettings()
                }
            )

            dispatch(HhdStore.Message.ProgressFinished)
        }
    }
}