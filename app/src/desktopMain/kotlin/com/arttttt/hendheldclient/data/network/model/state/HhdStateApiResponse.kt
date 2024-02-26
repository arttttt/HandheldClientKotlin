package com.arttttt.hendheldclient.data.network.model.state

import com.arttttt.hendheldclient.data.network.serializers.HhdStateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable(with = HhdStateSerializer::class)
data class HhdStateApiResponse(
    val states: JsonElement
)