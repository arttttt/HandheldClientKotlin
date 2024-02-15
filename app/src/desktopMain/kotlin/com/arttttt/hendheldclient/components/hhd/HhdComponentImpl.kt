package com.arttttt.hendheldclient.components.hhd

import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.entity.HhdPort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class HhdComponentImpl(
    context: AppComponentContext,
) : HhdComponent,
    AppComponentContext by context {

    init {
        val api = HhdApi(
            token = HhdAuthToken("24a8ba4c8ec3"),
            port = HhdPort(5335),
        )

        coroutineScope.launch {
            println(
                withContext(Dispatchers.IO) {
                    api.getSettings()
                }
            )
        }
    }
}