package com.arttttt.hendheldclient.ui.hhd

import com.arttttt.hendheldclient.arch.Transformer
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.domain.entity.settings.SettingField
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem

class HhdTransformer : Transformer<HhdStore.State, HhdComponent.UiState> {

    override fun invoke(state: HhdStore.State): HhdComponent.UiState {
        return HhdComponent.UiState(
            items = state.sections.map { (_, section) ->
                ContainerListItem(
                    id = section.id,
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
                                id = field.id,
                                title = field.title,
                                isChecked = field.getCorrectValue(
                                    pendingChanges = state.pendingChanges,
                                    parent = section.id,
                                ),
                            )

                            is SettingField.IntInputField -> IntListItem(
                                id = field.id,
                                title = field.title,
                                value = field
                                    .getCorrectValue(
                                        pendingChanges = state.pendingChanges,
                                        parent = section.id,
                                    )
                                    .toString(),
                                error = field.getError(
                                    pendingChanges = state.pendingChanges,
                                    parent = section.id,
                                ),
                                isValueOverwritten = isValueOverwritten(
                                    pendingChanges = state.pendingChanges,
                                    parent = section.id,
                                    field = field,
                                ),
                            )
                        }
                    },
                )
            }
        )
    }

    private fun <T> SettingField<T>.getCorrectValue(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
    ): T {
        val mapperValue: (Any?) -> Any? = when (this) {
            is SettingField.BooleanField -> {
                val result: (Any?) -> Boolean = { it as Boolean }

                result
            }

            is SettingField.IntInputField -> {
                val result: (Any?) -> String = {
                    (it as? String) ?: ""
                }

                result
            }

            else -> {
                val result: (Any?) -> Any? = { it }

                result
            }
        }

        return getCorrectValue(
            pendingChanges = pendingChanges,
            parent = parent,
            overwrittenMapper = mapperValue as (Any?) -> T,
        )
    }

    private fun <T> SettingField<T>.getCorrectValue(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
        overwrittenMapper: (Any?) -> T,
    ): T {
        return if (isValueOverwritten(pendingChanges, parent, this)) {
            pendingChanges
                .getValue(parent)
                .getValue(id)
                .let(overwrittenMapper)
        } else {
            value
        }
    }

    @Suppress("ReplaceGetOrSet")
    private fun isValueOverwritten(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
        field: SettingField<*>,
    ): Boolean {
        return pendingChanges.get(parent)?.containsKey(field.id) == true
    }

    private fun SettingField.IntInputField.getError(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
    ): String? {
        val currentValue = getCorrectValue(
            pendingChanges = pendingChanges,
            parent = parent,
        ).toIntOrNull() ?: 0

        return when {
            currentValue !in min..max -> "the value must be in the range: [$min, $max]"
            else -> null
        }
    }
}