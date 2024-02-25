package com.arttttt.hendheldclient.domain.entity.settings

data class FieldKey(
    val parent: FieldKey?,
    val key: String,
)