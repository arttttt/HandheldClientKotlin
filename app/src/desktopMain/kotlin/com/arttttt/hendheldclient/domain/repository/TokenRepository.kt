package com.arttttt.hendheldclient.domain.repository

import com.arttttt.hendheldclient.domain.entity.HhdAuthToken

interface TokenRepository {

    suspend fun getHhdToken(): HhdAuthToken
}