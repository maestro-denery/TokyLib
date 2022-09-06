plugins {
    id("java-platform")
    id("maven-publish")
}

dependencies {
    constraints {
        sequenceOf(
            "base",
            "data-addons"
        ).forEach {
            api(project(":toky-$it"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("toky-platform") {
            from(components["javaPlatform"])
        }
    }
}

