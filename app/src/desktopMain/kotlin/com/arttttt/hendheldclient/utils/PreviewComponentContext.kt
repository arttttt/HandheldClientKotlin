package com.arttttt.hendheldclient.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

object PreviewComponentContext : ComponentContext by DefaultComponentContext(
    lifecycle = LifecycleRegistry(),
)