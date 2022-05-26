plugins {
    id("io.github.reyerizo.gradle.jcstress") version "0.8.13"
}

version = "0.1.0"

jcstress {
    verbose = "true"
    spinStyle = "HARD"
}