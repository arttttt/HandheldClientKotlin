package com.arttttt.hendheldclient.data.network.model

import com.arttttt.hendheldclient.data.network.serializers.HhdSettingsSerializer
import kotlinx.serialization.Serializable

@Serializable(with = HhdSettingsSerializer::class)
data class HhdSettingsApiResponse(
    val settings: List<HhdContainerFieldApiResponse>
)