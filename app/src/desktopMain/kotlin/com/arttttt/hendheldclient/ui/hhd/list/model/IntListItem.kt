package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.utils.ListItem

data class IntListItem(
    val id: FieldKey,
    val title: String,
    val value: String,
    val error: String?,
    val isValueOverwritten: Boolean,
) : ListItem {
    override val key: String = id.key
}