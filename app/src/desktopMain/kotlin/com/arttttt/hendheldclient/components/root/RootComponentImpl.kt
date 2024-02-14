package com.arttttt.hendheldclient.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.context.defaultAppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.components.login.LoginComponentImpl
import com.arttttt.hendheldclient.components.main.MainComponentImpl
import kotlinx.serialization.Serializable

class RootComponentImpl(
    context: AppComponentContext,
) : RootComponent,
    AppComponentContext by context {

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Main : Config
    }

    private val koinScope = koinScope()

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
        val wrappedContext = defaultAppComponentContext(
            context = context,
            parentScopeID = koinScope.id,
        )

        return when (config) {
            is Config.Login -> LoginComponentImpl(
                context = wrappedContext,
                openNextScreen = {
                    navigation.replaceCurrent(Config.Main)
                },
            )

            is Config.Main -> MainComponentImpl(
                context = wrappedContext,
            )
        }
    }
}