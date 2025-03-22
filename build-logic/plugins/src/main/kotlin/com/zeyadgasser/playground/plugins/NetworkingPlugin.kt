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
                    implementation(libs.findLibrary("ktor.client.android"))
                }

                commonMain.dependencies {
                    implementation(libs.findLibrary("ktor.client.core"))
                    implementation(libs.findLibrary("ktor.client.content.negotiation"))
                    implementation(libs.findLibrary("ktor.serialization.kotlinx.json"))
                    implementation(libs.findLibrary("ktor.logging"))
                    implementation(libs.findLibrary("ktor.cio"))

                    implementation(libs.findLibrary("coil.compose"))
                }

                commonTest.dependencies {
                    implementation(libs.findLibrary("ktor.mock"))
                }

                iosMain.dependencies {
                    implementation(libs.findLibrary("ktor.client.darwin"))
                }
            }
        }
    }
}