import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.playground.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.sharedui"
    }

    val xcfName = "sharedUIKit"
    targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
        target.binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
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
