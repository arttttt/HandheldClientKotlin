package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.data.network.model.settings.HhdFieldApiResponse2
import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse2
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.domain.entity.settings.SettingField
import com.arttttt.hendheldclient.domain.entity.settings.SettingsSection
import com.arttttt.hendheldclient.domain.repository.HhdRepository
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class HhdRepositoryImpl(
    private val api: HhdApi,
) : HhdRepository {

    override suspend fun getSettings(): Map<String, SettingsSection> {
        val settings = api.getSettings()

        val state = api.getState()

        val result =  mapToEntity(
            settings = settings,
            state = state,
        )

        return result
    }

    private fun mapToEntity(
        settings: HhdSettingsApiResponse2,
        state: HhdStateApiResponse,
    ): Map<String, SettingsSection> {
        return buildMap {
            for ((key, value) in settings.settings) {
                val rootKey = FieldKey(
                    parent = null,
                    key = key,
                )

                putAll(
                    createSections(
                        rootKey = rootKey,
                        children = value,
                    )
                )
            }

            /*for ((key, value) in settings.hhd) {
                val fields = state.hhd[key] ?: continue

                put(
                    key,
                    SettingsSection(
                        id = key,
                        title = value.title,
                        tags = value.tags,
                        fields = createFields(
                            fields = fields,
                            children = value.children,
                        )
                    ),
                )
            }*/
        }
    }

    private fun createSections(
        rootKey: FieldKey,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingsSection> {
        return buildMap {
            for ((key, value) in children) {
                val sectionKey = FieldKey(
                    parent = rootKey,
                    key = key,
                )

                if (value !is HhdFieldApiResponse2.Container) continue

                put(
                    key,
                    SettingsSection(
                        key = sectionKey,
                        id = key,
                        title = value.title,
                        tags = value.tags,
                        fields = createFields(
                            rootKey = sectionKey,
                            fields = emptyMap(),
                            children = value.children,
                        )
                    ),
                )
            }
        }
    }

    private fun createFields(
        rootKey: FieldKey,
        fields: Map<String, JsonElement>,
        children: Map<String, HhdFieldApiResponse2>,
    ): Map<String, SettingField<*>> {
        return buildMap {
            for ((key, value) in children) {
                val field = fields[key]

                val fieldKey = FieldKey(
                    parent = rootKey,
                    key = key,
                )

                val parsed = when (value) {
                    is HhdFieldApiResponse2.Display -> SettingField.DisplayField(
                        key = fieldKey,
                        id = key,
                        value = field?.nullableContent ?: value.default?.nullableContent,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse2.Action -> SettingField.ActionField(
                        key = fieldKey,
                        id = key,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        value = field?.nullableBoolean ?: value.default?.nullableBoolean
                    )
                    is HhdFieldApiResponse2.BooleanPrimitive -> SettingField.BooleanField(
                        key = fieldKey,
                        id = key,
                        value = field?.jsonPrimitive?.boolean ?: value.default,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse2.IntPrimitive -> SettingField.IntInputField(
                        key = fieldKey,
                        id = key,
                        value = field?.jsonPrimitive?.content ?: value.default.jsonPrimitive.content,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        min = value.min,
                        max = value.max,
                        step = value.step?.jsonPrimitive?.int,
                    )
                    else -> continue
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