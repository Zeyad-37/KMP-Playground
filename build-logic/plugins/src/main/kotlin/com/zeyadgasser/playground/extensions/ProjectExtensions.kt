package com.zeyadgasser.playground.extensions

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.android(): BaseExtension =
    project.extensions.findByType(BaseExtension::class.java)
        ?: throw GradleException("Project $name is not an Android project")
