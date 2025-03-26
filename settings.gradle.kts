rootProject.name = "KMPPlayground"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:architecture")
include(":core:networking")
include(":core:sharedUI")
include(":features:task:domain")
include(":features:task:sharedPresentation")
include(":features:task:list")
include(":core:database")
include(":features:task:data")
include(":features:task:detail")
include(":features:user:profile")
include(":features:user:forgotPassword")
