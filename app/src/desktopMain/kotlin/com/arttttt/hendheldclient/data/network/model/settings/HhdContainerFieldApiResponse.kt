package com.arttttt.hendheldclient.data.network.model.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HhdContainerFieldApiResponse(
    @SerialName("title") val title: String,
    @SerialName("hint") val hint: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("children") val children: Map<String, HhdFieldApiResponse>,
)