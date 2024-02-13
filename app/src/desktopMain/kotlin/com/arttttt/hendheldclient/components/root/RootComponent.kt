package com.arttttt.hendheldclient.components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent

interface RootComponent : DecomposeComponent {
    
    val stack: Value<ChildStack<*, DecomposeComponent>>
}