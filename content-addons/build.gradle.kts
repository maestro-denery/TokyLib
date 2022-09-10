version = "0.1.0"

dependencies {
    compileOnly(project(":toky-base"))
    compileOnly(project(":toky-io"))
    testImplementation(project(":toky-io"))
    testImplementation(project(":toky-base"))
}