package com.arttttt.hendheldclient.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface NavigationItem {

    val icon: ImageVector
    val title: String

    data object HHD : NavigationItem {
        override val icon: ImageVector = Icons.Default.Settings
        override val title: String = "HHD"
    }

    data object About : NavigationItem {
        override val icon: ImageVector = Icons.Default.Info
        override val title: String = "About"
    }
}