package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.data.network.model.settings.HhdFieldApiResponse
import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
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
        settings: HhdSettingsApiResponse,
        state: HhdStateApiResponse,
    ): Map<String, SettingsSection> {
        return buildMap {
            for ((key, value) in settings.hhd) {
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
            }
        }
    }

    private fun createFields(
        fields: Map<String, JsonElement>,
        children: Map<String, HhdFieldApiResponse>,
    ): Map<String, SettingField<*>> {
        return buildMap {
            for ((key, value) in children) {
                val field = fields[key]

                val parsed = when (value) {
                    is HhdFieldApiResponse.Display -> SettingField.DisplayField(
                        id = key,
                        value = field?.nullableContent ?: value.default?.nullableContent,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse.Action -> SettingField.ActionField(
                        id = key,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse.BooleanPrimitive -> SettingField.BooleanField(
                        id = key,
                        value = field?.jsonPrimitive?.boolean ?: value.default,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                    )
                    is HhdFieldApiResponse.IntPrimitive -> SettingField.IntField(
                        id = key,
                        value = field?.jsonPrimitive?.int ?: value.default,
                        hint = value.hint,
                        tags = value.tags,
                        title = value.title,
                        min = value.min,
                        max = value.max,
                        step = value.step?.jsonPrimitive?.int,
                    )
                }

                put(
                    key = key,
                    value = parsed,
                )
            }
        }
    }

    private val JsonElement.nullableContent: String?
        get() {
            return this.takeIf { it != JsonNull }?.jsonPrimitive?.content
        }
}