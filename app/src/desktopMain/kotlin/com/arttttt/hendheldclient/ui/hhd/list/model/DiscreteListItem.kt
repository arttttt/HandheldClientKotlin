package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem

data class DiscreteListItem(
    val id: String,
    val title: String,
    val value: Int,
    val values: Set<Int>
) : ListItem {
    override val key: String = id
}