package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class MultipleListItem(
    val id: String,
    val title: String,
    val value: String,
    val values: Map<String, String>,
) : ListItem {
    override val key: String = id
}