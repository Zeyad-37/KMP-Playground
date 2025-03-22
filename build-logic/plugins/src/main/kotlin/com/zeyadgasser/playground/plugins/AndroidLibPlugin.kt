package com.zeyadgasser.playground.plugins

import com.android.build.api.dsl.LibraryExtension
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
//        pluginManager.apply(libs.findPlugin("androidLibrary").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("android.kotlin.multiplatform.library").get().get().pluginId)
        extensions.configure<LibraryExtension> {
            compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()
            defaultConfig {
                minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }
            packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

            buildTypes {
//                getByName("debug") {
//                    isDebuggable = true
//                }
                getByName("release") {
//                    isDebuggable = false
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            buildFeatures.buildConfig = true

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            lint {
                warningsAsErrors = false
                abortOnError = true
            }

            testOptions.apply {
                targetSdk = libs.findVersion("android.targetSdk").get().toString().toInt()
                animationsDisabled = true
                unitTests.isReturnDefaultValues = true
                unitTests.isIncludeAndroidResources = true
            }
        }
    }
}
