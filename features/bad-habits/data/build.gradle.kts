plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.room)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "badHabitsDataKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.badhabits.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":features:bad-habits:domain"))
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
