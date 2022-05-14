plugins {
    id("java-library")
}

group = "net.drf.drf-api"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    dependencies {
        api("net.kyori:adventure-key:4.10.1")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    tasks {
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
}
