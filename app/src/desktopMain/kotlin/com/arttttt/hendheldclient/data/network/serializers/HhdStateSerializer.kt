package com.arttttt.hendheldclient.data.network.serializers

import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder

@Suppress("NAME_SHADOWING")
object HhdStateSerializer : KSerializer<HhdStateApiResponse> {

    private const val INCORRECT_DECODER_MESSAGE = "decoder is not a json decoder"

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("HhdStateApiResponse") {}

    override fun deserialize(decoder: Decoder): HhdStateApiResponse {
        val decoder = decoder as? JsonDecoder ?: throw IllegalArgumentException(INCORRECT_DECODER_MESSAGE)

        return HhdStateApiResponse(
            state = decoder.decodeJsonElement()
        )
    }

    override fun serialize(encoder: Encoder, value: HhdStateApiResponse) {}
}