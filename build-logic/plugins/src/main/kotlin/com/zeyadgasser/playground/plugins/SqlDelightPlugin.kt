package com.zeyadgasser.playground.plugins

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("sqldelight").get().get().pluginId)
        }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                val desktopMain = getByName("desktopMain")
                desktopMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.jvm.driver").get())
                }
                val desktopTest = getByName("desktopTest")
                desktopTest.dependencies {
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
                    implementation(libs.findLibrary("sql.delight.coroutines.extensions.correct").get())
                }
                iosMain.dependencies {
                    implementation(libs.findLibrary("sql.delight.native.driver").get())
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
