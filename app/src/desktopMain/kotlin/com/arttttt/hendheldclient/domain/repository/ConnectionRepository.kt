package com.arttttt.hendheldclient.domain.repository

import com.arttttt.hendheldclient.domain.entity.HhdAuthToken
import com.arttttt.hendheldclient.domain.entity.HhdPort

interface ConnectionRepository {

    suspend fun getHhdToken(): HhdAuthToken
    fun getDefaultPort(): HhdPort
}