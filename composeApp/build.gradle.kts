import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "1.9.22"
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("io.ktor:ktor-client-core:3.0.0-wasm2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0-wasm2")
            implementation("io.ktor:ktor-client-content-negotiation:3.0.0-wasm2")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1-wasm0")
            implementation("org.jetbrains.compose.material3:material3-wasm-js:1.6.11")

        }
    }
}

//.\gradlew clean; .\gradlew wasmJsBrowserDistribution; copy ./composeApp/build/dist/wasmJs/productionExecutable/* ../links_server/src/main/resources/META-INF/resources/ -R -Force