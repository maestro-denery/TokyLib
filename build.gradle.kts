// TODO clean up the
plugins {
    id("java-library")
    id("maven-publish")
}

subprojects.filter { it.name != "drf-bom" }.forEach {
    it.apply(plugin = "java-library")
    it.apply(plugin = "maven-publish")

    it.repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
    }

    it.dependencies {
        compileOnlyApi("net.kyori:adventure-key:4.10.1")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    it.java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
        withJavadocJar()
    }

    it.tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
        javadoc {
            options.encoding = Charsets.UTF_8.name()
        }
        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }
        getByName<Test>("test") {
            useJUnitPlatform()
        }
    }

    it.publishing {
        publications {
            create<MavenPublication>(it.name) {
                groupId = "net.drf"
                artifactId = it.name

                artifact(it.tasks.getByName("sourcesJar")) {
                    classifier = "sources"
                }

                artifact(it.tasks.getByName("javadocJar")) {
                    classifier = "javadoc"
                }
                artifacts.artifact(it.tasks.jar) {
                    classifier = ""
                }

                //from(components["java"])

                pom {
                    licenses {
                        license {
                            name.set("Mozilla Public License Version 2.0")
                            url.set("https://mozilla.org/MPL/2.0/")
                        }
                    }
                }

                /*versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }*/
            }
        }
    }
}
