package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.data.network.model.settings.HhdFieldApiResponse2
import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse2
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField3
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class HhdRepositoryImpl(
    private val api: HhdApi,
) : HhdRepository {

    override suspend fun getSettings(): Map<String, SettingField3> {
        val settings = api.getSettings()

        val state = api.getState()

        val result = mapToEntity(
            settings = settings,
            state = state,
        )

        return result
    }

    private fun mapToEntity(
        settings: HhdSettingsApiResponse2,
        state: HhdStateApiResponse,
    ): Map<String, SettingField3> {
        return buildMap {
            for ((key, value) in settings.settings) {
                val rootKey = FieldKey(
                    parent = null,
                    key = key,
                )

                put(
                    key = key,
                    SettingField3.RootField(
                        key = rootKey,
                        items = parseRootData(
                            rootKey = rootKey,
                            children = value,
                        ),
                    )
                )
            }
        }
    }

    private fun parseRootData(
        rootKey: FieldKey,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingField3> {
        return buildMap {
            for ((key, value) in children) {
                if (value !is HhdFieldApiResponse2.Container) continue

                put(
                    key = key,
                    value = parseContainer(
                        key = FieldKey(
                            parent = rootKey,
                            key = key,
                        ),
                        container = value,
                    )
                )
            }
        }
    }

    private fun parseContainer(
        key: FieldKey,
        container: HhdFieldApiResponse2.Container,
    ): SettingField3 {
        return SettingField3.Fields.SectionField(
            key = key,
            title = container.title,
            tags = container.tags,
            hint = container.hint,
            value = createFields(
                rootKey = key,
                fields = emptyMap(),
                children = container.children,
            ),
        )
    }

    private fun createFields(
        rootKey: FieldKey,
        fields: Map<String, JsonElement>,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingField3> {
        return buildMap {
            for ((key, value) in children) {
                val field = fields[key]

                val fieldKey = FieldKey(
                    parent = rootKey,
                    key = key,
                )

                val parsed = when (value) {
                    is HhdFieldApiResponse2.Display -> SettingField3.Fields.DisplayField(
                        key = fieldKey,
                        value = field?.nullableContent ?: value.default?.nullableContent,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse2.Action -> SettingField3.Fields.ActionField(
                        key = fieldKey,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        value = field?.nullableBoolean ?: value.default?.nullableBoolean
                    )
                    is HhdFieldApiResponse2.BooleanPrimitive -> SettingField3.Fields.BooleanField(
                        key = fieldKey,
                        value = field?.jsonPrimitive?.boolean ?: value.default,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse2.IntPrimitive -> SettingField3.Fields.IntInputField(
                        key = fieldKey,
                        value = field?.jsonPrimitive?.content ?: value.default.jsonPrimitive.content,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        min = value.min,
                        max = value.max,
                        step = value.step?.jsonPrimitive?.int,
                    )
                    is HhdFieldApiResponse2.Container -> parseContainer(
                        key = fieldKey,
                        container = value,
                    )
                    is HhdFieldApiResponse2.Discrete -> SettingField3.Fields.DiscreteField(
                        key = fieldKey,
                        value = field?.jsonPrimitive?.int ?: value.default.jsonPrimitive.int,
                        values = value.options.toSet(),
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse2.Multiple -> SettingField3.Fields.MultipleField(
                        key = fieldKey,
                        value = field?.jsonPrimitive?.content ?: value.default.jsonPrimitive.content,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        values = value.options,
                    )
                    is HhdFieldApiResponse2.Mode -> SettingField3.Fields.Mode(
                        key = fieldKey,
                        value = field?.jsonPrimitive?.content ?: value.default.jsonPrimitive.content,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        modes = createFields(
                            rootKey = fieldKey,
                            fields = emptyMap(),
                            children = value.modes,
                        ),
                    )
                }

                put(
                    key = key,
                    value = parsed,
                )
            }
        }
    }

    private val JsonElement.nullableBoolean: Boolean?
        get() {
            return this.takeIf { it != JsonNull }?.jsonPrimitive?.boolean
        }

    private val JsonElement.nullableContent: String?
        get() {
            return this.takeIf { it != JsonNull }?.jsonPrimitive?.content
        }
}