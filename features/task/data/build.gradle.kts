plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.sql.delight)
    alias(libs.plugins.playground.room)
    alias(libs.plugins.playground.networking)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "taskDataKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.task.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(files("$rootDir/libs/crypto.aar"))
                implementation(project(":core:architecture"))
                implementation(project(":core:database"))
                implementation(project(":core:networking"))
                implementation(project(":features:task:domain"))
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

        androidHostTest {
            dependencies {
                implementation(libs.sql.delight.jvm.driver)
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
        jvmTest.dependencies {
            implementation(libs.sql.delight.jvm.driver)
        }
//        getByName("jvmTest") {
//            dependencies {
//            }
//        }
    }
}
