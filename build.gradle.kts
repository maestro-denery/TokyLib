plugins {
    id("java-library")
    id("maven-publish")
    id("io.github.reyerizo.gradle.jcstress") version "0.8.13"
}

jcstress {
    verbose = "true"
    // timeMillis = "200"
    spinStyle = "HARD"
}

allprojects.filter { it.name != "drf-bom" }.forEach {
    it.apply(plugin = "java-library")
    it.apply(plugin = "maven-publish")

    it.repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
    }

    it.dependencies {
        compileOnlyApi("net.kyori:adventure-key:4.10.1")
        compileOnlyApi("com.mojang:datafixerupper:4.1.27")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    it.java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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

    publishing {
        publications {
            create<MavenPublication>(it.name) {
                artifact(it.tasks.jar) {
                    groupId = "net.drf"
                    artifactId = it.name
                    version = project.properties["version"].toString()
                }
                pom {
                    licenses {
                        license {
                            name.set("Mozilla Public License Version 2.0")
                            url.set("https://mozilla.org/MPL/2.0/")
                        }
                    }
                }

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
            }
        }
    }
}
