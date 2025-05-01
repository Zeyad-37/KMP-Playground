package com.zeyadgasser.playground.plugins

import com.android.build.api.dsl.androidLibrary
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.UUID
import javax.inject.Inject

// Define an extension class to hold the configuration
open class SharedLibPluginExtension @Inject constructor(project: Project) {
    val xcfName: Property<String> = project.objects.property(String::class.java)
}

class SharedLibPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        // Register the extension
        val extension = extensions.create("sharedLib", SharedLibPluginExtension::class.java)

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
            js(IR) {
                browser()
            }
            iosX64()
            iosArm64()
            iosSimulatorArm64()
            targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
                target.binaries.framework {
                    baseName = extension.xcfName.getOrElse(UUID.randomUUID().toString())
                }
            }
            jvm() // desktop
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