plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.compose.multiplatform)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.navigation)
    alias(libs.plugins.playground.networking)
    alias(libs.plugins.playground.testing)
    alias(libs.plugins.kotlinxSerialization)
}

sharedLib.xcfName = "breathKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.breath"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
                implementation(project(":core:sharedUI"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
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

compose.resources {
    publicResClass = false
    packageOfResClass = "com.zeyadgasser.playground.breath.resources"
    generateResClass = auto
}
