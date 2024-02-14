package com.arttttt.hendheldclient.components.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arttttt.hendheldclient.arch.DecomposeComponent
import com.arttttt.hendheldclient.arch.context.AppComponentContext
import com.arttttt.hendheldclient.arch.context.defaultAppComponentContext
import com.arttttt.hendheldclient.arch.koinScope
import com.arttttt.hendheldclient.components.hhd.HhdComponentImpl
import com.arttttt.hendheldclient.ui.main.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class MainComponentImpl(
    context: AppComponentContext,
) : MainComponent,
    AppComponentContext by context {

    @Serializable
    private sealed interface Config {
        @Serializable
        data object HHD : Config
        @Serializable
        data object About : Config
    }

    private val koinScope = koinScope()

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, DecomposeComponent>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.HHD,
        childFactory = ::createChild,
    )

    override val states = MutableStateFlow(
        MainComponent.UiState(
            navigationItems = listOf(
                NavigationItem.HHD,
                NavigationItem.About,
            ),
            selectedItem = NavigationItem.HHD,
        )
    )

    override fun onItemClicked(item: NavigationItem) {
        states.update { state ->
            state.copy(
                selectedItem = item,
            )
        }
        navigation.replaceCurrent(item.toConfig())
    }

    private fun createChild(config: Config, context: ComponentContext): DecomposeComponent {
        val wrappedContext = defaultAppComponentContext(
            context = context,
            parentScopeID = koinScope.id,
        )

        return when (config) {
            is Config.HHD -> HhdComponentImpl(wrappedContext)
            is Config.About -> object : DecomposeComponent {}
        }
    }

    private fun NavigationItem.toConfig(): Config {
        return when (this) {
            NavigationItem.HHD -> Config.HHD
            NavigationItem.About -> Config.About
        }
    }
}