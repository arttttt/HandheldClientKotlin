package com.arttttt.hendheldclient.ui.hhd

import com.arttttt.hendheldclient.arch.Transformer
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField3
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.DiscreteListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ModeContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ModeListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.MultipleListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem
import com.arttttt.hendheldclient.utils.ListItem
import com.arttttt.hendheldclient.utils.mapKeysNutNull
import com.arttttt.hendheldclient.utils.mapValuesNutNull

class HhdTransformer : Transformer<HhdStore.State, HhdComponent.UiState> {

    override fun invoke(state: HhdStore.State): HhdComponent.UiState {
        return HhdComponent.UiState(
            items = state
                .fields
                .mapNotNull { (_, field) ->
                    field.toListItem(state)
                }
                .flatten()
                .filterNotNull()
        )
    }

    private fun SettingField3.toListItem(
        state: HhdStore.State
    ): List<ListItem?> {
        return when (val section = this) {
            is SettingField3.RootField -> section
                .items
                .map { (_, field) -> field.toListItem(state) }
                .flatten()
            is SettingField3.Fields<*> -> listOf(
                section.toListItem(
                    state = state,
                )
            )
        }
    }

    private fun SettingField3.Fields<*>.toListItem(
        state: HhdStore.State,
    ): ListItem? {
        return when (val section = this) {
            is SettingField3.Fields.SectionField -> section.toListItem(
                state = state,
            )
            is SettingField3.Fields.DisplayField -> section.toListItem()
            is SettingField3.Fields.ActionField -> section.toListItem()
            is SettingField3.Fields.BooleanField -> section.toListItem(
                state = state,
            )
            is SettingField3.Fields.IntInputField -> section.toListItem(
                state = state,
            )
            is SettingField3.Fields.DiscreteField -> section.toListItem(
                state = state
            )
            is SettingField3.Fields.MultipleField -> section.toListItem(
                state = state,
            )
            is SettingField3.Fields.Mode -> section.toListItem(
                state = state,
            )
        }
    }

    private fun SettingField3.Fields.SectionField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return ContainerListItem(
            id = this.key,
            title = this.title,
            children = this
                .value
                .mapNotNull { (_, field) -> field.toListItem(state) }
                .flatten()
                .filterNotNull()
        )
    }

    private fun SettingField3.Fields.DisplayField.toListItem(): ListItem {
        return TextListItem(
            title = this.title,
            value = this.value,
        )
    }

    private fun SettingField3.Fields.ActionField.toListItem(): ListItem {
        return ActionListItem(
            title = this.title,
            isEnabled = this.value == true,
        )
    }

    private fun SettingField3.Fields.BooleanField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return BooleanListItem(
            id = this.key,
            title = this.title,
            isChecked = getCorrectValue(
                pendingChanges = state.pendingChanges2,
            ),
        )
    }

    private fun SettingField3.Fields.IntInputField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return IntListItem(
            id = this.key,
            title = this.title,
            value = this
                .getCorrectValue(
                    pendingChanges = state.pendingChanges2,
                )
                .toString(),
            error = this.getError(
                pendingChanges = state.pendingChanges2,
            ),
            isValueOverwritten = this.isValueOverwritten(
                pendingChanges = state.pendingChanges2,
            )
        )
    }

    private fun SettingField3.Fields.DiscreteField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return DiscreteListItem(
            id = this.key,
            title = this.title,
            value = getCorrectValue(
                pendingChanges = state.pendingChanges2,
            ),
            values = this.values,
        )
    }

    private fun SettingField3.Fields.MultipleField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return MultipleListItem(
            id = this.key,
            title = this.title,
            value = getCorrectValue(
                pendingChanges = state.pendingChanges2,
            ),
            values = this.values,
        )
    }

    private fun SettingField3.Fields.Mode.toListItem(
        state: HhdStore.State,
    ): ListItem? {
        val mode = modes.getValue(
            getCorrectValue(
                pendingChanges = state.pendingChanges2
            )
        ) as? SettingField3.Fields.SectionField ?: return null

        return ModeListItem(
            id = this.key,
            title = this.title,
            modes = modes
                .mapKeysNutNull { (_, value) ->
                    when (value) {
                        is SettingField3.Fields<*> -> value.key
                        else -> null
                    }
                }
                .mapValuesNutNull { (_, value) ->
                    value.let { it as? SettingField3.Fields.SectionField }?.title
                },
            mode = ModeContainerListItem(
                id = mode.key,
                title = mode.title,
                children = mode
                    .value
                    .map { (_, field) -> field.toListItem(state) }
                    .flatten()
                    .filterNotNull(),
            ),
        )
    }

    private fun <T> SettingField3.Fields<T>.getCorrectValue(
        pendingChanges: Map<FieldKey, Any>,
    ): T {
        val mapperValue: (Any?) -> Any? = when (this) {
            is SettingField3.Fields.BooleanField -> {
                val result: (Any?) -> Boolean = { it as Boolean }

                result
            }

            is SettingField3.Fields.IntInputField -> {
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
            overwrittenMapper = mapperValue as (Any?) -> T,
        )
    }

    private fun <T> SettingField3.Fields<T>.getCorrectValue(
        pendingChanges: Map<FieldKey, Any>,
        overwrittenMapper: (Any?) -> T,
    ): T {
        return if (this.isValueOverwritten(pendingChanges)) {
            pendingChanges
                .getValue(this.key)
                .let(overwrittenMapper)
        } else {
            value
        }
    }

    private fun SettingField3.Fields<*>.isValueOverwritten(
        pendingChanges: Map<FieldKey, Any>,
    ): Boolean {
        return pendingChanges.containsKey(this.key)
    }

    private fun SettingField3.Fields.IntInputField.getError(
        pendingChanges: Map<FieldKey, Any>,
    ): String? {
        val currentValue = getCorrectValue(
            pendingChanges = pendingChanges,
        ).toIntOrNull() ?: 0

        return when {
            currentValue !in min..max -> "the value must be in the range: [$min, $max]"
            else -> null
        }
    }
}