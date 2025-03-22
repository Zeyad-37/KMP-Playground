package com.zeyadgasser.playground.plugins

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply<DetektPlugin>()
        configure<DetektExtension> {
            ignoreFailures = true
            file("$rootDir/config/detekt/detekt.yml")
                .takeIf { it.exists() }
                ?.let { config.from(it) }
            file("$rootDir/config/detekt/detekt-baseline.xml")
                .takeIf { it.exists() }
                ?.let { baseline = it }
        }
        project.dependencies.add("detektPlugins", project.project(":config:detekt:detektRules"))
        project.dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
        val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
            output.set(layout.buildDirectory.file("reports/detekt/merge.sarif"))
        }
        tasks.withType<Detekt>().configureEach {
                buildUponDefaultConfig = true
                config.setFrom(project.rootProject.files("config/detekt/detekt.yml"))
                autoCorrect = true
                parallel = true
                disableDefaultRuleSets = false
                debug = false

                val sourceCode = listOf(
                    project.rootProject.allprojects.map { it.file("src") },
                    project.rootProject.allprojects.map { it.file("build.gradle.kts") },
                    project.rootProject.file("settings.gradle.kts"),
                    project.rootProject.file("build-logic/src"),
                    project.rootProject.file("build-logic/build.gradle.kts"),
                    project.rootProject.file("build-logic/settings.gradle.kts")
                )
                source = project.files(sourceCode).asFileTree
                // This is relative to src/ of each module, see how production `source` is set up just above.
                include("**/*.kt")
                include("**/*.kts")
                file("$rootDir/config/detekt/detekt-baseline.xml")
                    .takeIf { it.exists() }
                    ?.let { baseline.set(it) }
                jvmTarget = JavaVersion.VERSION_17.toString()
                reports {
                    html.required.set(true)
                    sarif.required.set(true)
                }
                basePath = rootDir.absolutePath
                finalizedBy(detektReportMergeSarif)
            }
            detektReportMergeSarif { input.from(tasks.withType<Detekt>().map { it.sarifReportFile }) }
            tasks.withType<DetektCreateBaselineTask>().configureEach {
                jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}
