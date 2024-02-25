package com.arttttt.hendheldclient.data.network.serializers

import com.arttttt.hendheldclient.data.network.model.settings.HhdFieldApiResponse2
import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse2
import com.arttttt.hendheldclient.utils.mapValuesNutNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("NAME_SHADOWING")
object HhdSettingsSerializer2 : KSerializer<HhdSettingsApiResponse2> {

    private const val INCORRECT_DECODER_MESSAGE = "decoder is not a json decoder"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("HhdSettingsApiResponse2") {
    }

    override fun deserialize(decoder: Decoder): HhdSettingsApiResponse2 {
        val decoder = decoder as? JsonDecoder ?: throw IllegalArgumentException(INCORRECT_DECODER_MESSAGE)

        val result = decoder
            .decodeJsonElement()
            .jsonObject
            .mapValuesNutNull { (_, rootJElement) ->
                rootJElement.jsonObject.mapValuesNutNull { (_, jElement) ->
                    jElement.jsonObject.toField()
                }
            }

        return HhdSettingsApiResponse2(
            settings = result
        )
    }

    override fun serialize(encoder: Encoder, value: HhdSettingsApiResponse2) {}

    private fun JsonObject.toField(): HhdFieldApiResponse2? {
        val type = this["type"]?.jsonPrimitive?.content
        return when (type) {
            "container" -> this.toContainer()
            "display" -> json.decodeFromJsonElement<HhdFieldApiResponse2.Display>(this)
            "action" -> json.decodeFromJsonElement<HhdFieldApiResponse2.Action>(this)
            "bool" -> json.decodeFromJsonElement<HhdFieldApiResponse2.BooleanPrimitive>(this)
            "int" -> json.decodeFromJsonElement<HhdFieldApiResponse2.IntPrimitive>(this)
            "discrete" -> json.decodeFromJsonElement<HhdFieldApiResponse2.Discrete>(this)
            else -> {
                println(
                    """
                        unsupported type: $type
                    """.trimIndent()
                )

                null
            }
        }
    }

    private fun JsonObject.toContainer(): HhdFieldApiResponse2.Container {
        val title = this["title"]!!.jsonPrimitive.content
        val hint = this["hint"]!!.jsonPrimitive.content
        val tags = this["tags"]!!.jsonArray.map { it.jsonPrimitive.content }
        val children = this["children"]
            ?.jsonObject
            ?.mapValuesNutNull { (_, child) ->
                child.jsonObject.toField()
            }
            ?: emptyMap()

        return HhdFieldApiResponse2.Container(
            title = title,
            hint = hint,
            tags = tags,
            children = children,
        )
    }
}