package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import dev.mokkery.gradle.mokkery
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class NetworkingPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinxSerialization").get().get().pluginId)
        }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                androidMain.dependencies {
                    implementation(libs.findLibrary("ktor.client.android").get())
                }

                commonMain.dependencies {
                    implementation(libs.findLibrary("ktor.client.core").get())
                    implementation(libs.findLibrary("ktor.client.content.negotiation").get())
                    implementation(libs.findLibrary("ktor.serialization.kotlinx.json").get())
                    implementation(libs.findLibrary("ktor.logging").get())
                    implementation(libs.findLibrary("ktor.cio").get())

                    implementation(libs.findLibrary("coil.compose").get())
                }

                commonTest.dependencies {
                    implementation(libs.findLibrary("ktor.mock").get())
                }

                iosMain.dependencies {
                    implementation(libs.findLibrary("ktor.client.darwin").get())
                }
            }
        }
    }
}