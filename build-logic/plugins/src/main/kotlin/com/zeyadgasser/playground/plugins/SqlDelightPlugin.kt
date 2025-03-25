package com.zeyadgasser.playground.plugins

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
//        with(pluginManager) { // fixme
//            apply(libs.findPlugin("sqldelight").get().get().pluginId)
//        }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                jvmMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.jvm.driver").get())
                }
                jvmTest.dependencies {
                    implementation(libs.findLibrary("sql.delight.jvm.driver").get())
                }
                androidMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.android.driver").get())
                }
                androidUnitTest.dependencies {
                    implementation(libs.findLibrary("sql.delight.jvm.driver").get())
                }
                commonMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.runtime").get())
                    implementation(libs.findLibrary("sql.delight.coroutines.extensions").get()) // todo check!
                    implementation(libs.findLibrary("sql.delight.coroutines.extensions.correct").get())
                }
                iosMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.native.driver").get())
                }
                jsMain.dependencies {
                    implementation("app.cash.sqldelight:sqljs-driver:2.0.0-alpha05")
                    implementation(npm("sql.js", "1.6.2"))
                    implementation(devNpm("copy-webpack-plugin", "9.1.0"))


//                    implementation("app.cash.sqldelight:sqljs-driver:2.0.2")
//                    implementation(npm("sql.js", "1.6.2"))
//                    implementation(devNpm("copy-webpack-plugin", "9.1.0"))

//                    implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.2"))
//                    implementation(npm("sql.js", "1.8.0"))
//                    implementation("app.cash.sqldelight:web-worker-driver:2.0.2")

                }
            }
        }
        extensions.configure<SqlDelightExtension> {
            linkSqlite.set(true)
            databases.create("PlaygroundDB") {
                packageName.set("com.zeyadgasser.playground.tasks.data.db")
            }
        }
    }
}
