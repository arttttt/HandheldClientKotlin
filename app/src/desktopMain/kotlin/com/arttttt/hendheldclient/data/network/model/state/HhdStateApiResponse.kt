package com.arttttt.hendheldclient.data.network.model.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class HhdStateApiResponse(
    @SerialName("hhd") val hhd: Map<String, Map<String, JsonElement>>
)