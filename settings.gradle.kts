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
include(":core:utils")
include(":features:task:domain")
include(":features:task:sharedPresentation")
include(":features:task:list")
include(":features:task:data")
include(":features:task:detail")
include(":features:user:profile")
include(":features:user:forgotPassword")
include(":features:user:signInUp")
include(":features:user:data")
include(":features:user:domain")
include(":features:settings")
include(":features:breath")
include(":features:routine:domain")
include(":features:routine:data")
include(":features:routine:sharedPresentation")
include(":features:routine:detail")
include(":features:routine:list")
include(":features:routine:form")
include(":features:bad-habits:domain")
include(":features:bad-habits:data")
include(":features:bad-habits:sharedPresentation")
include(":features:bad-habits:list")
include(":features:bad-habits:form")
include(":features:bad-habits:detail")

