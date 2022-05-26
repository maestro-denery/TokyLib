plugins {
    id("io.github.reyerizo.gradle.jcstress") version "0.8.13"
}

version = "1.0.0"

jcstress {
    verbose = "true"
    spinStyle = "HARD"
}