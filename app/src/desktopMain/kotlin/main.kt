import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arttttt.hendheldclient.arch.context.defaultAppComponentContext
import com.arttttt.hendheldclient.components.root.RootComponent
import com.arttttt.hendheldclient.components.root.RootComponentImpl
import com.arttttt.hendheldclient.di.mviModule
import com.arttttt.hendheldclient.di.repositoryModule
import com.arttttt.hendheldclient.di.storeModule
import com.arttttt.hendheldclient.ui.root.RootContent
import org.koin.core.context.startKoin

@ExperimentalDecomposeApi
fun main() {
    startKoin {
        modules(
            mviModule,
            storeModule,
            repositoryModule,
        )
    }

    val lifecycle = LifecycleRegistry()

    val root: RootComponent = RootComponentImpl(
        context = defaultAppComponentContext(
            context = DefaultComponentContext(lifecycle),
            parentScopeID = null,
        )
    )

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Handheld client"
        ) {
            MaterialTheme {
                RootContent(root)
            }
        }
    }
}