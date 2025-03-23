package com.zeyadgasser.playground.plugins

import com.android.build.api.dsl.androidLibrary
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SharedLibPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("android-kotlin-multiplatform-library").get().get().pluginId)
        }
        configureKotlin()
        extensions.configure<KotlinMultiplatformExtension> {
            androidLibrary {
                compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()
                minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
                withHostTestBuilder {}
                withDeviceTestBuilder {
                    sourceSetTreeName = "test"
                }.configure {
                    instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            iosX64()
            iosArm64()
            iosSimulatorArm64()
            jvm("desktop")
            compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    private fun Project.configureKotlin() {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                val warningsAsErrors: String? by project
                allWarningsAsErrors.set(warningsAsErrors.toBoolean())
                freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
            }
        }
    }
}