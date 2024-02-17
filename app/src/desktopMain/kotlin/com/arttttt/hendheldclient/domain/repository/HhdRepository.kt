package com.arttttt.hendheldclient.domain.repository

import com.arttttt.hendheldclient.domain.entity.settings.SettingsSection

interface HhdRepository {

    suspend fun getSettings(): Map<String, SettingsSection>
}