package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.repository.TokenRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.coroutines.resume

class TokenRepositoryImpl : TokenRepository {

    companion object {

        private const val DEFAULT_PATH = ".config/hhd/token"
    }

    private val userHome by lazy {
        System.getProperty("user.home")
    }

    private val defaultHhdPath by lazy {
        "${userHome}/$DEFAULT_PATH".toPath()
    }

    override suspend fun getHhdToken(): HhdAuthToken {
        return suspendCancellableCoroutine { continuation ->
            FileSystem.SYSTEM.read(defaultHhdPath) {
                continuation.resume(
                    HhdAuthToken(
                        token = readUtf8()
                    )
                )
            }
        }
    }
}