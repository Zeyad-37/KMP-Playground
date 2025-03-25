import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.playground.multiplatform.lib)
}

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.architecture"
    }

    val xcfName = "architectureKit"
    targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
        target.binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlin.stdlib)
                api(libs.kotlinx.coroutines.core)
                api(libs.napier)
                api(libs.androidx.lifecycle.viewmodel)
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
