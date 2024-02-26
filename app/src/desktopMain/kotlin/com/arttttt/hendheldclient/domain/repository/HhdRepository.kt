package com.arttttt.hendheldclient.domain.repository

import com.arttttt.hendheldclient.domain.entity.settings.SettingField3

interface HhdRepository {

    suspend fun getSettings(): Map<String, SettingField3>
}