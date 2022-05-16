pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "DRF"

// Libraries / APIs
include("registries")
include("data-addon")

// Implementations
include("paper-impl")
