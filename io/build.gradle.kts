dependencies {
    // implementation("org.mongodb:mongo-java-driver:3.12.11")
    compileOnly(project(":toky-base"))
    testImplementation(project(":toky-base"))
}