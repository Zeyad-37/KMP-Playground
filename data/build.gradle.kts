import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.sql.delight)
    alias(libs.plugins.playground.networking)
    alias(libs.plugins.playground.testing)
}

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.task.data"
    }

    val xcfName = "dataKit"
    targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
        target.binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(files("../libs/crypto.aar"))
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
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
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
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }

        getByName("desktopTest") {
            dependencies {
                implementation(libs.sql.delight.jvm.driver)
            }
        }
    }

}