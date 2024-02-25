package com.arttttt.hendheldclient.domain.entity.settings

data class SettingFieldRoot(
    val key: FieldKey,
    val items: Map<String, SettingField2<*>>
)