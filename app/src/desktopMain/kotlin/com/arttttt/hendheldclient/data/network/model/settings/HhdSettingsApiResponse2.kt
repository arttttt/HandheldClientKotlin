package com.arttttt.hendheldclient.data.network.model.settings

import com.arttttt.hendheldclient.data.network.serializers.HhdSettingsSerializer2
import kotlinx.serialization.Serializable

@Serializable(with = HhdSettingsSerializer2::class)
data class HhdSettingsApiResponse2(
    val settings: Map<String, Map<String, HhdFieldApiResponse2>>
)