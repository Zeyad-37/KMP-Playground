package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                androidMain.dependencies {
                    implementation(libs.findLibrary("koin.androidx.workmanager").get())
                    implementation(libs.findLibrary("koin.androidx.compose").get())
                }

                commonMain.dependencies {
                    implementation(libs.findLibrary("koin.core").get())
                    implementation(libs.findLibrary("koin.compose").get())
//                    implementation(libs.findLibrary("koin.compose.viewmodel").get()) // breaks IOS
//                    implementation(libs.findLibrary("koin.compose.viewmodel.navigation").get()) // breaks IOS
                }
            }
        }
    }
}
