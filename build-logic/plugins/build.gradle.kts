import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    `kotlin-dsl`
}

group = "com.zeyadgasser.playground.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin.compilerOptions.jvmTarget = JVM_17

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

dependencies {
    compileOnly(libs.plugins.kotlinxSerialization.toDep())
    compileOnly(libs.plugins.androidApplication.toDep())
    compileOnly(libs.plugins.androidLibrary.toDep())
    compileOnly(libs.plugins.composeMultiplatform.toDep())
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.composeCompiler.toDep())
    compileOnly(libs.plugins.sqldelight.toDep())
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.plugins.mokkery.toDep())
    compileOnly(libs.plugins.all.open.toDep())
    compileOnly(libs.plugins.detekt.toDep())
    compileOnly(libs.plugins.koverage.toDep())
    compileOnly(libs.detekt.api)
    testImplementation(libs.detekt.api)
    testImplementation(libs.detekt.test)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("koin") {
            id = libs.plugins.playground.koin.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.KoinPlugin"
            displayName = "Koin Plugin"
            description = "Plugin configures koin for a multiplatform project"
        }
        register("androidLib") {
            id = libs.plugins.playground.android.library.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.AndroidLibPlugin"
            displayName = "Android Lib Plugin"
            description = "Plugin configures an android project"
        }
        register("sharedLib") {
            id = libs.plugins.playground.multiplatform.lib.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.SharedLibPlugin"
            displayName = "SharedLib Plugin"
            description = "Plugin configures a multiplatform shared lib project"
        }
        register("koverage") {
            id = libs.plugins.playground.koverage.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.KoveragePlugin"
            displayName = "Koverage Plugin"
            description = "Plugin configures Koverage for a project"
        }
        register("testing") {
            id = libs.plugins.playground.testing.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.TestingPlugin"
            displayName = "Testing Plugin"
            description =
                "Plugin configures Kotlin Test, CoroutinesTest, Turbine & Mockery for an multiplatform project"
        }
        register("networking") {
            id = libs.plugins.playground.networking.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.NetworkingPlugin"
            displayName = "Networking Plugin"
            description = "Plugin configures Ktor and Coil for a multiplatform project"
        }
        register("navigation") {
            id = libs.plugins.playground.navigation.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.NavigationPlugin"
            displayName = "Navigation Plugin"
            description = "Plugin configures Voyager for a multiplatform project"
        }
        register("sqldelight") {
            id = libs.plugins.playground.sql.delight.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.SqlDelightPlugin"
            displayName = "SqlDelight Plugin"
            description = "Plugin configures SqlDelight for a multiplatform project"
        }
        register("room") {
            id = libs.plugins.playground.room.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.RoomPlugin"
            displayName = "Room Plugin"
            description = "Plugin configures Room for a multiplatform project"
        }
        register("gitHooks") {
            id = libs.plugins.playground.git.hooks.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.GitHooksPlugin"
            displayName = "GitHooks Plugin"
            description = "Plugin configures GitHooks for a project"
        }
        register("composeMultiplatform") {
            id = libs.plugins.playground.compose.multiplatform.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.ComposeMultiplatformPlugin"
            displayName = "ComposeMultiplatform Plugin"
            description = "Plugin configures ComposeMultiplatform for a multiplatform project"
        }
        register("detekt") {
            id = libs.plugins.playground.detekt.get().pluginId
            implementationClass = "com.zeyadgasser.playground.plugins.DetektPlugin"
            displayName = "Detekt Plugin"
            description = "Plugin configures Detekt for a project"
        }
    }
}
