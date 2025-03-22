package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SharedLibPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<ComposeMultiplatformPlugin>()
        apply<AndroidLibPlugin>()
        apply<KoinPlugin>()
        apply<NetworkingPlugin>()
        apply<SqlDelightPlugin>()
        apply<NavigationPlugin>()
        apply<TestingPlugin>()
        with(pluginManager) {

        }
        configureKotlin()
        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget().compilerOptions.jvmTarget.set(JvmTarget.JVM_17)

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

            jvm("desktop")

            compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

            sourceSets.apply {
                androidMain.dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.android"))
                }

                commonMain.dependencies {
//                    implementation(libs.findLibrary("androidx.lifecycle.viewmodel"))
//                    implementation(libs.findLibrary("kotlinx.datetime"))
                    implementation(libs.findLibrary("kotlinx.coroutines.core"))

                    implementation(libs.findLibrary("napier"))
                }
                val desktopMain = getByName("desktopMain")
                desktopMain.dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.swing"))

                }
            }
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
//            kotlinOptions {
//                jvmTarget = JavaVersion.VERSION_17.toString()
//                val warningsAsErrors: String? by project
//                allWarningsAsErrors = warningsAsErrors.toBoolean()
//                freeCompilerArgs = freeCompilerArgs + listOf(
//                    "-opt-in=kotlin.RequiresOptIn",
//                    // Enable experimental coroutines APIs, including Flow
//                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//                    "-opt-in=kotlinx.coroutines.FlowPreview",
//                )
//            }
        }
    }
}