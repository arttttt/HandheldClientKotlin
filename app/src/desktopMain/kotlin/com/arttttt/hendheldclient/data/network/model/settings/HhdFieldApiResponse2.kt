package com.arttttt.hendheldclient.data.network.model.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed interface HhdFieldApiResponse2 {

    @Serializable
    data class Container(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("children") val children: Map<String, HhdFieldApiResponse2>,
    ) : HhdFieldApiResponse2

    @Serializable
    data class Display(
        @SerialName("default") val default: JsonElement?,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("title") val title: String,
    ) : HhdFieldApiResponse2

    @Serializable
    data class Action(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: JsonElement?,
    ) : HhdFieldApiResponse2

    @Serializable
    data class BooleanPrimitive(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: Boolean,
    ) : HhdFieldApiResponse2

    @Serializable
    data class IntPrimitive(
        @SerialName("title") val title: String,
        @SerialName("hint") val hint: String,
        @SerialName("tags") val tags: List<String>,
        @SerialName("default") val default: JsonElement,
        @SerialName("min") val min: Int,
        @SerialName("max") val max: Int,
        @SerialName("step") val step: JsonElement?,
    ) : HhdFieldApiResponse2
}