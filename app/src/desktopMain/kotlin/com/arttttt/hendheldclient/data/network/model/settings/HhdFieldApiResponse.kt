package com.arttttt.hendheldclient.data.network.model.settings

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement

@Serializable
sealed interface HhdFieldApiResponse {

    @Serializable
    data class Display(
        @SerialName("default") val default: JsonElement?,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("title") val title: String,
    ) : HhdFieldApiResponse

    @Serializable
    data class Action(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: JsonElement?,
    ) : HhdFieldApiResponse

    @Serializable
    data class BooleanPrimitive(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: Boolean,
    ) : HhdFieldApiResponse

    @Serializable
    data class IntPrimitive(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: Int,
        @SerialName("min") val min: Int,
        @SerialName("max") val max: Int,
        @SerialName("step") val step: JsonElement?,
    ) : HhdFieldApiResponse
}
