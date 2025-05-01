plugins {
    alias(libs.plugins.playground.multiplatform.lib)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.playground.koin)
    alias(libs.plugins.playground.sql.delight)
    alias(libs.plugins.playground.testing)
}

sharedLib.xcfName = "databaseKit"

kotlin {
    androidLibrary {
        namespace = "com.zeyadgasser.playground.database"
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
