package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.data.network.model.settings.HhdFieldApiResponse2
import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse2
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import com.arttttt.hendheldclient.data.network.model.state.HhdStateRequestBody
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField3
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * todo: move out parsing
 */
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

    override suspend fun applyOverrides(overrides: Map<FieldKey, Any>) {
        api.setState(
            body = HhdStateRequestBody(
                state = overrides.mapKeys { (key, _) ->
                    key.toPathString()
                }
            )
        )
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

                val states = state.state.jsonObject.getValue(key)

                put(
                    key = key,
                    value = SettingField3.RootField(
                        key = rootKey,
                        items = parseRootData(
                            rootKey = rootKey,
                            states = states,
                            children = value,
                        ),
                    )
                )
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    private fun parseRootData(
        rootKey: FieldKey,
        states: JsonElement?,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingField3> {
        return buildMap {
            for ((key, value) in children) {
                if (value !is HhdFieldApiResponse2.Container) continue

                val states = states?.jsonObject?.get(key)

                put(
                    key = key,
                    value = parseContainer(
                        key = FieldKey(
                            parent = rootKey,
                            key = key,
                        ),
                        states = states,
                        container = value,
                    )
                )
            }
        }
    }

    private fun parseContainer(
        key: FieldKey,
        states: JsonElement?,
        container: HhdFieldApiResponse2.Container,
    ): SettingField3 {
        return SettingField3.Fields.SectionField(
            key = key,
            title = container.title,
            tags = container.tags,
            hint = container.hint,
            value = createFields(
                rootKey = key,
                fields = states?.jsonObject,
                children = container.children,
            ),
        )
    }

    private fun createFields(
        rootKey: FieldKey,
        fields: JsonObject?,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingField3> {
        return buildMap {
            for ((key, value) in children) {
                val field = fields?.get(key)

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
                        states = field?.jsonObject,
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
                        value = field?.jsonObject?.get("mode")?.nullableContent ?: value.default.jsonPrimitive.content,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        modes = createFields(
                            rootKey = fieldKey,
                            fields = field?.jsonObject,
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

    private fun FieldKey.toPathString(): String {
        return buildString {
            appendKeyPath(this@toPathString)
        }
    }

    private fun StringBuilder.appendKeyPath(fieldKey: FieldKey) {
        fieldKey.parent?.let {
            appendKeyPath(it)
            append('.')
        }
        append(fieldKey.key)
    }
}