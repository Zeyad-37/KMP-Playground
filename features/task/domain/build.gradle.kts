plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "taskDomainKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.task.domain"
    }
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:architecture"))
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
