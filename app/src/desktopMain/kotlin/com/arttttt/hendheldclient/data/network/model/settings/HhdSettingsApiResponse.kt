package com.arttttt.hendheldclient.data.network.model.settings

import com.arttttt.hendheldclient.data.network.serializers.HhdSettingsSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = HhdSettingsSerializer::class)
data class HhdSettingsApiResponse(
    @SerialName("hhd") val hhd: Map<String, HhdContainerFieldApiResponse>
)