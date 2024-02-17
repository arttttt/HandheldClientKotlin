package com.arttttt.hendheldclient.ui.hhd.list.model

import com.arttttt.hendheldclient.utils.ListItem
import kotlin.random.Random

data class ContainerListItem(
    val title: String,
    val children: List<ListItem>
) : ListItem {
    override val key: String = title + Random.nextLong()
}