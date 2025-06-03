package com.zeyadgasser.playground.plugins

import com.zeyadgasser.playground.extensions.libs
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KoveragePlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
//        apply(plugin = "org.jetbrains.kotlinx.kover")
        pluginManager.apply(libs.findPlugin("koverage").get().get().pluginId)
        extensions.configure(KoverProjectExtension::class) {
            currentProject.sources.includedSourceSets.addAll("main")
            reports {
                verify.warningInsteadOfFailure.set(false)
                verify {
                    rule {
                        // add rules here?
                    }
                }
                total {
                    html {
                        title.set("$title Kover Report")
                        onCheck.set(false)
                        charset.set("UTF-8")
                        htmlDir.set(layout.buildDirectory.dir("reports/kover/html-result"))
                    }
                }
                filters {
                    excludes.classes.addAll(
                        "**/R.class",
                        "**/R\$*.class",
                        "**/BuildConfig.*",
                        "**/Manifest*.*",
//                        "**/*_Hilt*.class",
//                        "**/Hilt_*.class",
                    )
                }
            }
        }
    }
}