package com.arttttt.hendheldclient.ui.hhd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arttttt.hendheldclient.components.hhd.HhdComponent

@Composable
fun HhdContent(component: HhdComponent) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}