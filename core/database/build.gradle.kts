import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.sql.delight)
    alias(libs.plugins.playground.testing)
}

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.database"
    }

    val xcfName = "databaseKit"

    targets.filterIsInstance<KotlinNativeTarget>().forEach { target: KotlinNativeTarget ->
        target.binaries.framework {
            baseName = xcfName
        }
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

sqldelight {
    linkSqlite = true
    databases {
        val dbName = "PlaygroundDB"
        findByName(dbName) ?: create(dbName) {
            packageName.set("com.zeyadgasser.playground.tasks.data.db")
        }
    }
}
