package com.arttttt.hendheldclient.data.network.serializers

import com.arttttt.hendheldclient.data.network.model.HhdContainerFieldApiResponse
import com.arttttt.hendheldclient.data.network.model.HhdFieldApiResponse
import com.arttttt.hendheldclient.data.network.model.HhdSettingsApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("NAME_SHADOWING")
object HhdSettingsSerializer : KSerializer<HhdSettingsApiResponse> {

    private const val INCORRECT_DECODER_MESSAGE = "decoder is not a json decoder"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("HhdSettingsApiResponse") {
        element<JsonElement>("hhd")
    }

    override fun deserialize(decoder: Decoder): HhdSettingsApiResponse {
        val decoder = decoder as? JsonDecoder ?: throw IllegalArgumentException(INCORRECT_DECODER_MESSAGE)

        val jObject = decoder.decodeJsonElement().jsonObject.getValue("hhd").jsonObject

        return HhdSettingsApiResponse(
            settings = jObject.map { (_, jElement) ->
                jElement.jsonObject.toContainer()
            }
        )
    }

    override fun serialize(encoder: Encoder, value: HhdSettingsApiResponse) {}

    private fun JsonObject.toContainer(): HhdContainerFieldApiResponse {
        val title = this["title"]!!.jsonPrimitive.content
        val hint = this["hint"]!!.jsonPrimitive.content
        val tags = this["tags"]!!.jsonArray.map { it.jsonPrimitive.content }
        val children = this["children"]
            ?.jsonObject
            ?.mapNotNull { (_, child) ->
                child.jsonObject.toField()
            } ?: listOf()

        return HhdContainerFieldApiResponse(
            title = title,
            hint = hint,
            tags = tags,
            children = children,
        )
    }

    private fun JsonObject.toField(): HhdFieldApiResponse? {
        val type = this["type"]?.jsonPrimitive?.content
        return when (type) {
            "display" -> json.decodeFromJsonElement<HhdFieldApiResponse.Display>(this)
            "action" -> json.decodeFromJsonElement<HhdFieldApiResponse.Action>(this)
            "bool" -> json.decodeFromJsonElement<HhdFieldApiResponse.BooleanPrimitive>(this)
            "int" -> json.decodeFromJsonElement<HhdFieldApiResponse.IntPrimitive>(this)
            else -> null
        }
    }
}