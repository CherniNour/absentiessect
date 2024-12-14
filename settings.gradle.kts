pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven ( "https://jitpack.io") // Add this line to resolve dependencies from jitpack.io (for MPAndroidChart, etc.)
        maven("https://repo.maven.apache.org/maven2")

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    maven("https://repo.maven.apache.org/maven2")
    }
}

rootProject.name = "AbsentiEssect1"
include(":app")
