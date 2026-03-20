plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

application {
    mainClass.set("io.github.hoaithu842.rachel.remotecompose.server.MainKt")
}

dependencies {
    implementation("androidx.compose.remote:remote-core:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-creation-core:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-creation-jvm:1.0.0-alpha06")
}