package com.arttttt.hendheldclient.domain.repository

import com.arttttt.hendheldclient.domain.entity.settings.SettingFieldRoot

interface HhdRepository {

    suspend fun getSettings(): Map<String, SettingFieldRoot>
}