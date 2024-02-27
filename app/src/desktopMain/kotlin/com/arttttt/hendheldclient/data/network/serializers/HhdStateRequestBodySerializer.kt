package com.arttttt.hendheldclient.data.network.serializers

import com.arttttt.hendheldclient.data.network.model.state.HhdStateRequestBody
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object HhdStateRequestBodySerializer : KSerializer<HhdStateRequestBody> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("HhdStateRequestBody") {
        element("state", MapSerializer(String.serializer(), JsonElement.serializer()).descriptor)
    }

    override fun serialize(encoder: Encoder, value: HhdStateRequestBody) {
        val map = value.state.mapValues { (_, value) ->
            when (value) {
                is Int -> JsonPrimitive(value)
                is Double -> JsonPrimitive(value)
                is Float -> JsonPrimitive(value)
                is Boolean -> JsonPrimitive(value)
                else -> JsonPrimitive(value.toString())
            }
        }
        encoder.encodeSerializableValue(MapSerializer(String.serializer(), JsonElement.serializer()), map)
    }

    override fun deserialize(decoder: Decoder): HhdStateRequestBody {
        throw UnsupportedOperationException("Deserialization is not supported")
    }
}