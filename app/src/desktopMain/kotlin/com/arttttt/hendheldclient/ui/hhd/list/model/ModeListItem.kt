package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.domain.entity.settings.FieldKey
import com.arttttt.hendheldclient.utils.ListItem

data class ModeListItem(
    val id: FieldKey,
    val title: String,
    val modes: Map<FieldKey, String>,
    val mode: ModeContainerListItem,
) : ListItem {
    override val key: String = id.key
}