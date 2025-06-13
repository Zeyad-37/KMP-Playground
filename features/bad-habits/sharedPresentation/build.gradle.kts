plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "badHabitsSharedPresentationKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.badhabits.sharedpresentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":features:bad-habits:domain"))
                implementation(project(":features:bad-habits:data"))
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
