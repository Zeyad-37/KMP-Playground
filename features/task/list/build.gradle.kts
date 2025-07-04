plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.compose.multiplatform)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.navigation)
    alias(libs.plugins.playground.networking)
    alias(libs.plugins.playground.testing)
    alias(libs.plugins.kotlinxSerialization)
}

sharedLib.xcfName = "taskListKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.task.list"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
                implementation(project(":core:sharedUI"))
                implementation(project(":features:task:domain"))
                implementation(project(":features:task:sharedPresentation"))
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
    packageOfResClass = "com.zeyadgasser.playground.resources"
    generateResClass = auto
}
