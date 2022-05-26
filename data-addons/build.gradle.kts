version = "0.1.0"

dependencies {
    compileOnlyApi("com.mojang:datafixerupper:4.1.27")
    api("com.google.guava:guava:31.1-jre")
    api("com.lmax:disruptor:3.4.4")

    testImplementation("com.google.guava:guava:31.1-jre")
    testImplementation("com.lmax:disruptor:3.4.4")
}