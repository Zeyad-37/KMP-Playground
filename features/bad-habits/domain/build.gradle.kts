plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "badHabitsDomainKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.badhabits.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:sharedUI"))
                api(project(":core:architecture"))
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
