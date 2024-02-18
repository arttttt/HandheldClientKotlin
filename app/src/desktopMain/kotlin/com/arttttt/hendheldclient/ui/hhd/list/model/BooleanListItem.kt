package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class BooleanListItem(
    val id: String,
    val title: String,
    val isChecked: Boolean,
) : ListItem {
    override val key: String = title
}