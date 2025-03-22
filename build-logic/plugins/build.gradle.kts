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
    compileOnly(libs.plugins.mokkery.toDep())
    compileOnly(libs.plugins.all.open.toDep())
    compileOnly(libs.plugins.ktlint.toDep())

//    compileOnly(plugin("io.gitlab.arturbosch.detekt", "1.23.8"))
//    implementation(plugin("org.jetbrains.kotlinx.kover", "0.9.1"))
//    compileOnly(libs.android.gradlePlugin)
//    compileOnly(libs.android.tools.common)
//    compileOnly(libs.compose.gradlePlugin)
//    compileOnly(libs.firebase.crashlytics.gradlePlugin)
//    compileOnly(libs.firebase.performance.gradlePlugin)
//    compileOnly(libs.kotlin.gradlePlugin)
//    compileOnly(libs.ksp.gradlePlugin)
//    compileOnly(libs.room.gradlePlugin)
//    compileOnly(libs.detekt.api)
//    compileOnly(libs.kotlinx.coroutines.core)
////    implementation(libs.truth)
////    lintChecks(libs.androidx.lint.gradle)
//
//    testImplementation(libs.detekt.api)
//    testImplementation(libs.detekt.test)
}

fun DependencyHandler.plugin(id: String, version: String) = create("$id:$id.gradle.plugin:$version")

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
//        register("hilt") {
//            id = libs.plugins.playground.hilt.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.HiltPlugin"
//            displayName = "Hilt Plugin"
//            description = "Plugin configures hilt for an android project"
//        }
//        register("androidLib") {
//            id = libs.plugins.playground.android.library.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.AndroidLibPlugin"
//            displayName = "Android Lib Plugin"
//            description = "Plugin configures an android project"
//        }
//        register("featureLib") {
//            id = libs.plugins.playground.android.feature.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.FeatureLibPlugin"
//            displayName = "Android Feature Lib Plugin"
//            description = "Plugin configures an android feature project"
//        }
//        register("junit5") {
//            id = libs.plugins.playground.junit.jupiter.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.JUnitJupiterPlugin"
//            displayName = "JUnitJupiter Plugin"
//            description = "Plugin configures JUnitJupiter for an android project"
//        }
//        register("testing") {
//            id = libs.plugins.playground.testing.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.TestingPlugin"
//            displayName = "Testing Plugin"
//            description = "Plugin configures Mockito, JUnit5, CoroutinesTest & Turbine for an android project"
//        }
//        register("room") {
//            id = libs.plugins.playground.android.room.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.RoomPlugin"
//            displayName = "Room Plugin"
//            description = "Plugin configures Room an android project"
//        }
//        register("gitHooks") {
//            id = libs.plugins.playground.git.hooks.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.GitHooksPlugin"
//            displayName = "GitHooks Plugin"
//            description = "Plugin configures GitHooks a project"
//        }
//        register("jetpackCompose") {
//            id = libs.plugins.playground.android.compose.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.ComposePlugin"
//            displayName = "Jetpack Compose Plugin"
//            description = "Plugin configures Jetpack Compose a project"
//        }
//        register("detekt") {
//            id = libs.plugins.playground.detekt.get().pluginId
//            implementationClass = "com.zeyadgasser.playground.plugins.DetektPlugin"
//            displayName = "Detekt Plugin"
//            description = "Plugin configures Detekt for a project"
//        }
    }
}
