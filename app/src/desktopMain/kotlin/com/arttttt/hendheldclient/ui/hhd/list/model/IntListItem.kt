package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class IntListItem(
    val id: String,
    val title: String,
    val value: String,
    val error: String?,
    val isValueOverwritten: Boolean,
) : ListItem {
    override val key: String = id
}