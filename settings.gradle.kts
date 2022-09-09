pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "tokylib-parent"

sequenceOf(
    "bom",
    "base",
    "content-addons",
    "io"
).forEach {
    include("toky-$it")
    project(":toky-$it").projectDir = file(it)
}
