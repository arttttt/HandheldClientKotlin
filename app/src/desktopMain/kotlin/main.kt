import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arttttt.hendheldclient.components.root.RootComponent
import com.arttttt.hendheldclient.components.root.RootComponentImpl
import com.arttttt.hendheldclient.ui.root.RootContent

@ExperimentalDecomposeApi
fun main() {
    val lifecycle = LifecycleRegistry()

    val root: RootComponent = RootComponentImpl(
        context = DefaultComponentContext(lifecycle)
    )

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Handheld client"
        ) {
            RootContent(root)
        }
    }
}