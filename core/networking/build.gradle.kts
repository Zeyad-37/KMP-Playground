plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.networking)
}

sharedLib.xcfName = "networkingKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyad.playground.networking"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
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
