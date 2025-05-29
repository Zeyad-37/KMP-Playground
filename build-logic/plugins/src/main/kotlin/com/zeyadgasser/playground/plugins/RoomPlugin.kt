package com.zeyadgasser.playground.plugins

import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.google.devtools.ksp")
//        pluginManager.apply(libs.findPlugin("room.gradle").get().get().pluginId)
//        pluginManager.apply("androidx.room")
        extensions.configure<KspExtension> {
            arg("room.generateKotlin", "true")
        }
//        extensions.configure<RoomExtension> {
            // The schemas directory contains a schema file for each version of the Room database.
            // This is required to enable Room auto migrations.
            // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
//            schemaDirectory("$projectDir/schemas")
//        }

        dependencies {
            "ksp"(libs.findLibrary("androidx.room.compiler").get())
        }
//        dependencies {
//            add("kspAndroid", libs.findLibrary("androidx.room.compiler"))
//            add("kspIosSimulatorArm64", libs.findLibrary("androidx.room.compiler"))
//            add("kspIosX64", libs.findLibrary("androidx.room.compiler"))
//            add("kspIosArm64", libs.findLibrary("androidx.room.compiler"))
//        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                jvmMain.dependencies {

                }
                jvmTest.dependencies {

                }
                androidMain.dependencies {

                }
                androidUnitTest.dependencies {

                }
                commonMain.dependencies {
                    implementation(libs.findLibrary("androidx.room.runtime").get())

                    implementation(libs.findLibrary("androidx.sqlite.bundled").get())
                    runtimeOnly(libs.findLibrary("androidx.sqlite").get())
                    runtimeOnly(libs.findLibrary("androidx.sqlite.ktx").get())
//                    runtimeOnly("androidx.sqlite:sqlite:2.5.1")
//                    runtimeOnly("androidx.sqlite:sqlite-ktx:2.5.1")
//                    "ksp"(libs.findLibrary("androidx.room.compiler").get())
                }
                commonTest.dependencies {
                    implementation(libs.findLibrary("androidx.room.testing").get())
                }
                iosMain.dependencies {

                }
                jsMain.dependencies {

                }
            }
        }
    }
}
