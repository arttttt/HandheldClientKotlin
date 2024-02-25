package com.arttttt.hendheldclient.domain.entity.settings

data class SettingsSection(
    val key: FieldKey,
    val id: String,
    val title: String,
    val tags: List<String>,
    val fields: Map<String, SettingField<*>>
)