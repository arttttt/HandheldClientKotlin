import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm("desktop")

    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.arkivanov.decompose.core)
            implementation(libs.arkivanov.decompose.compose)
            implementation(libs.arkivanov.essenty.lifecycle)
            implementation(libs.arkivanov.mviKotlin.core)
            implementation(libs.arkivanov.mviKotlin.main)
            implementation(libs.arkivanov.mviKotlin.coroutines)
            implementation(libs.arkivanov.mviKotlin.logging)
            implementation(libs.arkivanov.mviKotlin.timetravel)

            implementation(libs.square.okio)

            implementation(libs.koin.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.json)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.slf4j.simple)
        }
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.arttttt.handheldclient"
            packageVersion = "1.0.0"
        }
    }
}
