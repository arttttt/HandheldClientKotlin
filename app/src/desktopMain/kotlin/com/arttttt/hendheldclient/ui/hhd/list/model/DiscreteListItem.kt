package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.utils.ListItem

data class DiscreteListItem(
    val id: FieldKey,
    val title: String,
    val value: Int,
    val values: Set<Int>
) : ListItem {
    override val key: String = id.key
}