plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "routineSharedPresentationKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.routine.sharedpresentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":features:routine:domain"))
                implementation(project(":features:routine:data"))
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
