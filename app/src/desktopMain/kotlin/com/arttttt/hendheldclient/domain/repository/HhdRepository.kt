package com.arttttt.hendheldclient.domain.repository

interface HhdRepository {

    suspend fun getSettings(): String
    suspend fun getState(): String
}