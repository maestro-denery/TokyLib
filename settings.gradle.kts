pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "drf-core"

sequenceOf(
    "bom",
    "data-addons",
    "lightweight-data-addons",
    "registries"
).forEach {
    include("drf-$it")
    project(":drf-$it").projectDir = file(it)
}
