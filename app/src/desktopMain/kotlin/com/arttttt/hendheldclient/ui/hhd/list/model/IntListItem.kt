package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class IntListItem(
    val title: String,
    val value: String,
) : ListItem {
    override val key: String = title
}