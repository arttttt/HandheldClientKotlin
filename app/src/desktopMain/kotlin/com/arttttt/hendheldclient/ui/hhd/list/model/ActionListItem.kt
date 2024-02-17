package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class ActionListItem(
    val title: String,
    val isEnabled: Boolean,
) : ListItem {
    override val key: String = title
}