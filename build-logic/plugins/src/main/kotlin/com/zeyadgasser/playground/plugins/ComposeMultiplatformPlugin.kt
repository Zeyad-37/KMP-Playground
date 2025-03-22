package com.zeyadgasser.playground.plugins

import com.android.build.gradle.BaseExtension
import com.zeyadgasser.playground.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

@OptIn(ExperimentalKotlinGradlePluginApi::class)
class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("composeCompiler").get().get().pluginId)
        }
        dependencies {
//            debugImplementation(compose.uiTooling)
        }
        extensions.configure<DesktopExtension> {
            application {
                mainClass = "com.zeyadgasser.playground.MainKt"
                nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                    packageName = "com.zeyadgasser.playground"
                    packageVersion = "1.0.0"
                }
            }
        }
        val compose = extensions.getByType<ComposeExtension>().dependencies
        extensions.configure<BaseExtension> {
            buildFeatures.compose = true
        }
        configureComposeCompiler()
        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
                instrumentedTestVariant {
                    sourceSetTree.set(KotlinSourceSetTree.test)
                    dependencies {
//                        implementation(libs.androidx.ui.test.junit4.android)
//                        debugImplementation(libs.androidx.ui.test.manifest)
                    }
                }
            }
            sourceSets.apply {
                androidMain.dependencies {
                    implementation(libs.findLibrary("androidx.activity.compose").get())
                    implementation(libs.findLibrary("androidx.compose.material3").get())
                    implementation(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                }
                commonMain.dependencies {
                    implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                    implementation(compose.runtime)
                    implementation(compose.foundation)
//                    implementation(compose.material)
                    implementation(compose.material3)
                    implementation(compose.animation)
                    implementation(compose.ui)
                    implementation(compose.components.resources)
                    implementation(compose.components.uiToolingPreview)
                    api(compose.materialIconsExtended)
                }
                commonTest.dependencies {
                    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                    implementation(compose.uiTest)
                }
                val desktopMain = getByName("desktopMain")
                desktopMain.dependencies {
                    implementation(compose.desktop.currentOs)
                }
            }
        }

    }

    fun Project.configureComposeCompiler() {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
            fun Provider<*>.relativeToRootProject(dir: String) = map {
                isolated.rootProject.projectDirectory
                    .dir("build")
                    .dir(projectDir.toRelativeString(rootDir))
            }.map { it.dir(dir) }

            project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
                .relativeToRootProject("compose-metrics")
                .let(metricsDestination::set)

            project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
                .relativeToRootProject("compose-reports")
                .let(reportsDestination::set)

            stabilityConfigurationFiles
                .add(isolated.rootProject.projectDirectory.file("compose_compiler_config.conf"))
        }
    }
}