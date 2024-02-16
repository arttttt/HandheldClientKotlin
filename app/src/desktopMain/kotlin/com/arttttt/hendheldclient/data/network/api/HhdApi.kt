package com.arttttt.hendheldclient.data.network.api

import com.arttttt.hendheldclient.data.network.model.settings.HhdSettingsApiResponse
import com.arttttt.hendheldclient.data.network.model.state.HhdStateApiResponse
import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.entity.HhdPort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HhdApi(
    private val token: HhdAuthToken,
    private val port: HhdPort,
) {

    /**
     * todo: make address configurable
     */
    private val baseUrl = "http://127.0.0.1/api/v1/"

    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(token.token, "")
                }
            }
        }

        defaultRequest {
            url(baseUrl)

            port = this@HhdApi.port.port
        }
    }

    suspend fun getSettings(): HhdSettingsApiResponse {
        return client.get("settings").body()
    }

    suspend fun getState(): HhdStateApiResponse {
        return client.get("state").body()
    }
}