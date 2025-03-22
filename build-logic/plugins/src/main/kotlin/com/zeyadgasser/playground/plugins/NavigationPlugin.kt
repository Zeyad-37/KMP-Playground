package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class NavigationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.findLibrary("voyager.navigator"))
                    implementation(libs.findLibrary("voyager.screenmodel"))
                    implementation(libs.findLibrary("voyager.koin"))
                }
            }
        }
    }
}
