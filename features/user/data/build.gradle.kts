import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.compose.multiplatform)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.navigation)
    alias(libs.plugins.playground.networking)
    alias(libs.plugins.playground.testing)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.user.data"
    }
    val xcfName = "dataKit"
    targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
        target.binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
                implementation(project(":features:user:domain"))
                implementation(libs.firebase.auth) // doesnt support wasm
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation("com.google.firebase:firebase-auth-ktx:23.2.0")
                implementation("com.google.firebase:firebase-common-ktx:21.0.0")
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.test.junit)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
