package com.arttttt.hendheldclient.data.repository

import com.arttttt.hendheldclient.data.network.api.HhdApi
import com.arttttt.hendheldclient.domain.repository.HhdRepository

class HhdRepositoryImpl(
    private val api: HhdApi,
) : HhdRepository {

    override suspend fun getSettings(): String {
        return api.getSettings()
    }

    override suspend fun getState(): String {
        return api.getState()
    }
}