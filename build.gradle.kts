plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

tasks.getByPath("app:run").dependsOn("copyNativeLibs")
tasks.getByPath("app:createDistributable").finalizedBy("copyNativeLibs")