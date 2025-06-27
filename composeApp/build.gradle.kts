@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.diffplug.gradle.spotless.SpotlessExtension
import dev.mokkery.gradle.mokkery
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.all.open)
    alias(libs.plugins.spotless)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
//        unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
            dependencies {
                implementation(libs.androidx.ui.test.junit4.android)
                debugImplementation(libs.androidx.ui.test.manifest)
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm() // desktop

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)

            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.work.runtime)

            implementation(libs.koin.androidx.workmanager)
            implementation(libs.koin.androidx.compose)

            implementation(libs.kotlinx.coroutines.android)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.sql.delight.jvm.driver)
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.espresso.core)
        }

        commonMain.dependencies {
            implementation(project(":core:architecture"))
            implementation(project(":core:networking"))
            implementation(project(":core:sharedUI"))
            implementation(project(":core:database"))
            implementation(project(":core:utils"))
            implementation(project(":features:task:data"))
            implementation(project(":features:task:domain"))
            implementation(project(":features:task:list"))
            implementation(project(":features:task:detail"))
            implementation(project(":features:task:sharedPresentation"))
            implementation(project(":features:breath"))
            implementation(project(":features:routine:data"))
            implementation(project(":features:routine:domain"))
            implementation(project(":features:routine:form"))
            implementation(project(":features:routine:list"))
            implementation(project(":features:routine:detail"))
            implementation(project(":features:routine:sharedPresentation"))
            implementation(project(":features:bad-habits:form"))
            implementation(project(":features:bad-habits:list"))
            implementation(project(":features:bad-habits:detail"))
            implementation(project(":features:bad-habits:sharedPresentation"))
            implementation(project(":features:bad-habits:domain"))
            implementation(project(":features:bad-habits:data"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.compose.navigation)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.common)
            implementation(libs.kotlin.test.annotations.common)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(mokkery("coroutines"))
            implementation(libs.ktor.mock)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(project(":core:database"))
            implementation(project(":core:networking"))
            implementation(project(":features:task:data"))
            implementation(project(":features:task:domain"))
        }

        iosMain.dependencies {
        }
        iosTest.dependencies {
            implementation(libs.sql.delight.native.driver)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
        jvmTest.dependencies {
            implementation(libs.sql.delight.jvm.driver)
        }
    }
}

val isTesting = gradle.startParameter.taskNames.any { it.endsWith("Test") }
if (isTesting) allOpen { annotation("com.zeyadgasser.playground.architecture.utils.OpenForMokkery") }

android {
    namespace = "com.zeyadgasser.playground"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.zeyadgasser.playground"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        isCoreLibraryDesugaringEnabled = true
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    lint {
        warningsAsErrors = false
        abortOnError = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.zeyadgasser.playground.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.zeyadgasser.playground"
            packageVersion = "1.0.0"
        }
    }
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt")
        targetExclude("bin/**/*.kt")
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_filename" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
            ),
        )
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
