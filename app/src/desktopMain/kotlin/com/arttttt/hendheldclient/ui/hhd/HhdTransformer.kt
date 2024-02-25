package com.arttttt.hendheldclient.ui.hhd

import com.arttttt.hendheldclient.arch.Transformer
import com.arttttt.hendheldclient.components.hhd.HhdComponent
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
                .sections
                .mapNotNull { (_, field) -> field.toListItem() }
        )
    }

    private fun SettingField2<*>.toListItem(): ListItem {
        return when (val section = this) {
            is SettingField2.SectionField -> section.toListItem()
            is SettingField2.DisplayField -> section.toListItem()
            is SettingField2.ActionField -> section.toListItem()
            is SettingField2.BooleanField -> section.toListItem()
            is SettingField2.IntInputField -> section.toListItem()
            is SettingField2.DiscreteField -> section.toListItem()
            is SettingField2.MultipleField -> section.toListItem()
            is SettingField2.Mode -> section.toListItem()
        }
    }

    private fun SettingField2.SectionField.toListItem(): ListItem {
        return ContainerListItem(
            id = this.key,
            title = this.title,
            children = this.value.mapNotNull { (_, field) -> field.toListItem() }
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

    private fun SettingField2.BooleanField.toListItem(): ListItem {
        return BooleanListItem(
            id = this.key,
            title = this.title,
            isChecked = false,
        )
    }

    private fun SettingField2.IntInputField.toListItem(): ListItem {
        return IntListItem(
            id = this.key,
            title = this.title,
            value = "50",
            error = null,
            isValueOverwritten = false,
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

    private fun SettingField2.Mode.toListItem(): ListItem {
        return ModeListItem(
            id = this.key,
            title = this.title,
            children = listOf(
                this.mode.toListItem()
            )
        )
    }

    private fun <T> SettingField2<T>.getCorrectValue(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
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
            parent = parent,
            overwrittenMapper = mapperValue as (Any?) -> T,
        )
    }

    private fun <T> SettingField2<T>.getCorrectValue(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
        overwrittenMapper: (Any?) -> T,
    ): T {
        return if (isValueOverwritten(pendingChanges, parent, this)) {
            pendingChanges
                .getValue(parent)
                .getValue(key.key)
                .let(overwrittenMapper)
        } else {
            value
        }
    }

    @Suppress("ReplaceGetOrSet")
    private fun isValueOverwritten(
        pendingChanges: Map<String, Map<String, Any?>>,
        parent: String,
        field: SettingField2<*>,
    ): Boolean {
        return pendingChanges.get(parent)?.containsKey(field.key.key) == true
    }

    private fun SettingField2.IntInputField.getError(
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

    /*ContainerListItem(
                        id = section.id,
                        title = section.title,
                        children = section.value.mapNotNull { (_, field) ->
                            when (field) {
                                is SettingField2.DisplayField -> TextListItem(
                                    title = field.title,
                                    value = field.value,
                                )

                                is SettingField2.ActionField -> ActionListItem(
                                    title = field.title,
                                    isEnabled = field.value == true,
                                )

                                is SettingField2.BooleanField -> BooleanListItem(
                                    id = field.id,
                                    title = field.title,
                                    isChecked = field.getCorrectValue(
                                        pendingChanges = state.pendingChanges,
                                        parent = section.id,
                                    ),
                                )

                                is SettingField2.IntInputField -> IntListItem(
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
                                is SettingField2.DiscreteField -> DiscreteListItem(
                                    id = field.id,
                                    title = field.title,
                                    value = field.value,
                                    values = field.values,
                                )
                                is SettingField2.MultipleField -> MultipleListItem(
                                    id = field.id,
                                    title = field.title,
                                    value = field.value,
                                    values = field.values,
                                )
                                is SettingField2.Mode -> {
                                    println(field)

                                    null
                                }
                                is SettingField2.SectionField -> null
                            }
                        },
                    )*/
}