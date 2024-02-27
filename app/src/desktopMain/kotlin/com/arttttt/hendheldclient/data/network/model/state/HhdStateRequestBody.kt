package com.arttttt.hendheldclient.data.network.model.state

import com.arttttt.hendheldclient.data.network.serializers.HhdStateRequestBodySerializer
import kotlinx.serialization.Serializable

@Serializable(HhdStateRequestBodySerializer::class)
data class HhdStateRequestBody(
    val state: Map<String, Any>,
)