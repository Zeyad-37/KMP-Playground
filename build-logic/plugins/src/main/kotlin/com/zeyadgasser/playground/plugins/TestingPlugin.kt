package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import dev.mokkery.gradle.mokkery
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class TestingPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply("dev.mokkery")
        plugins.apply("org.jetbrains.kotlin.plugin.allopen")
//        apply(plugin = "dev.mokkery")
//        with(pluginManager) {
//            apply(libs.findPlugin("mokkery").get().get().pluginId)
//            apply(libs.findPlugin("org.jetbrains.kotlin.plugin.allopen").get().get().pluginId)
//            apply(libs.findPlugin("all-open").get().get().pluginId)
//        }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin.test").get())
//                    implementation(kotlin("test"))
//                    implementation(kotlin("test-common"))
//                    implementation(kotlin("test-annotations-common"))
                    implementation(libs.findLibrary("kotlinx.coroutines.test").get())
                    implementation(libs.findLibrary("turbine").get())
                    implementation(mokkery("coroutines"))
                }

                androidUnitTest.dependencies {
                    implementation(libs.findLibrary("junit").get())
                    implementation(libs.findLibrary("androidx.test.junit").get())
                    implementation(libs.findLibrary("androidx.espresso.core").get())
                }
            }
        }
        val isTesting = gradle.startParameter.taskNames.any { it.endsWith("Test") }
        if (isTesting) extensions.configure<org.jetbrains.kotlin.allopen.gradle.AllOpenExtension> {
            annotation("com.zeyadgasser.playground.architecture.utils.OpenForMokkery")
        }
    }
}