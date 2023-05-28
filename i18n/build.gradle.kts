dependencies {
    implementation(project(":event"))
    implementation(project(":util"))

    compileOnly(libs.adventure)
    compileOnly(libs.aikar.commands)
    compileOnly(project(":object")) // only required for OneSky's JSON parsing
    compileOnly(libs.onesky)
    compileOnly(libs.redisson)
}
