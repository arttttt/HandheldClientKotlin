package com.arttttt.hendheldclient.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.components.login.LoginComponentImpl
import kotlinx.serialization.Serializable

class RootComponentImpl(
    context: ComponentContext,
) : RootComponent,
    ComponentContext by context {

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Login : Config
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, DecomposeComponent>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        childFactory = ::createChild,
    )

    private fun createChild(
        config: Config,
        context: ComponentContext
    ): DecomposeComponent {
        return when (config) {
            is Config.Login -> LoginComponentImpl(context)
        }
    }
}