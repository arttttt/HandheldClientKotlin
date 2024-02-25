package com.arttttt.hendheldclient.ui.hhd

import com.arttttt.hendheldclient.arch.Transformer
import com.arttttt.hendheldclient.components.hhd.HhdComponent
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField2
import com.arttttt.hendheldclient.domain.store.hhd.HhdStore
import com.arttttt.hendheldclient.ui.hhd.list.model.ActionListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.BooleanListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ContainerListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.DiscreteListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.IntListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.ModeListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.MultipleListItem
import com.arttttt.hendheldclient.ui.hhd.list.model.TextListItem
import com.arttttt.hendheldclient.utils.ListItem

class HhdTransformer : Transformer<HhdStore.State, HhdComponent.UiState> {

    override fun invoke(state: HhdStore.State): HhdComponent.UiState {
        return HhdComponent.UiState(
            items = state
                .fields
                .mapNotNull { (_, fieldRoot) ->
                    fieldRoot.items.mapNotNull { (_, field) ->
                        field.toListItem(state)
                    }
                }
                .flatten()
        )
    }

    private fun SettingField2<*>.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return when (val section = this) {
            is SettingField2.SectionField -> section.toListItem(
                state = state,
            )
            is SettingField2.DisplayField -> section.toListItem()
            is SettingField2.ActionField -> section.toListItem()
            is SettingField2.BooleanField -> section.toListItem(
                state = state,
            )
            is SettingField2.IntInputField -> section.toListItem(
                state = state,
            )
            is SettingField2.DiscreteField -> section.toListItem()
            is SettingField2.MultipleField -> section.toListItem()
            is SettingField2.Mode -> section.toListItem(
                state = state,
            )
        }
    }

    private fun SettingField2.SectionField.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return ContainerListItem(
            id = this.key,
            title = this.title,
            children = this.value.mapNotNull { (_, field) -> field.toListItem(state) }
        )
    }

    private fun SettingField2.DisplayField.toListItem(): ListItem {
        return TextListItem(
            title = this.title,
            value = this.value,
        )
    }

    private fun SettingField2.ActionField.toListItem(): ListItem {
        return ActionListItem(
            title = this.title,
            isEnabled = this.value == true,
        )
    }

    private fun SettingField2.BooleanField.toListItem(
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

    private fun SettingField2.IntInputField.toListItem(
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

    private fun SettingField2.DiscreteField.toListItem(): ListItem {
        return DiscreteListItem(
            id = this.key,
            title = this.title,
            value = this.value,
            values = this.values,
        )
    }

    private fun SettingField2.MultipleField.toListItem(): ListItem {
        return MultipleListItem(
            id = this.key,
            title = this.title,
            value = this.value,
            values = this.values,
        )
    }

    private fun SettingField2.Mode.toListItem(
        state: HhdStore.State,
    ): ListItem {
        return ModeListItem(
            id = this.key,
            title = this.title,
            children = listOf(
                this.mode.toListItem(state)
            )
        )
    }

    private fun <T> SettingField2<T>.getCorrectValue(
        pendingChanges: Map<FieldKey, Any>,
    ): T {
        val mapperValue: (Any?) -> Any? = when (this) {
            is SettingField2.BooleanField -> {
                val result: (Any?) -> Boolean = { it as Boolean }

                result
            }

            is SettingField2.IntInputField -> {
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

    private fun <T> SettingField2<T>.getCorrectValue(
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

    private fun SettingField2<*>.isValueOverwritten(
        pendingChanges: Map<FieldKey, Any>,
    ): Boolean {
        return pendingChanges.containsKey(this.key)
    }

    private fun SettingField2.IntInputField.getError(
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