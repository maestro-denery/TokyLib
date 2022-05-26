plugins {
    id("java-platform")
    id("maven-publish")
}

dependencies {
    constraints {
        sequenceOf(
            "base",
            "data-addons",
            "registries"
        ).forEach {
            api(project(":drf-$it"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("drf-platform") {
            from(components["javaPlatform"])
        }
    }
}

