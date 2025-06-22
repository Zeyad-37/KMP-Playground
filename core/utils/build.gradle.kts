plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.koin)
}

sharedLib.xcfName = "utilsKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.utils"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.datetime)
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
