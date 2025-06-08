@file:Suppress("UnstableApiUsage")

/*
* Created By nedaluof  31/5/2024.
*/
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
      maven {
        url = uri("https://jitpack.io")
      }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
      maven {
        url = uri("https://jitpack.io")
      }
    }
}

rootProject.name = "Qurany-Compose"
include(":app", ":data")