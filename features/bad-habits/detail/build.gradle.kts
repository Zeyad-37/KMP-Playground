plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.compose.multiplatform)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.navigation)
    alias(libs.plugins.playground.testing)
    alias(libs.plugins.kotlinxSerialization)
}

sharedLib.xcfName = "badHabitsDetailKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.badhabits.detail"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":features:bad-habits:domain"))
                implementation(project(":features:bad-habits:sharedPresentation"))
                implementation(project(":core:architecture"))
                implementation(project(":core:sharedUI"))
                implementation(libs.compose.charts)
                implementation(libs.kotlinx.datetime)
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
//    publicResClass = false
    packageOfResClass = "com.zeyadgasser.playground.badhabits.detail.resources"
    generateResClass = auto
}
