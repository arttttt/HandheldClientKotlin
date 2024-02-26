package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.utils.ListItem

data class ModeContainerListItem(
    val id: FieldKey,
    val title: String,
    val children: List<ListItem>
) : ListItem {
    override val key: String = id.key
}