plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.hoaithu842.rachel.remotecompose.controller"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.hoaithu842.rachel.remotecompose.controller"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation("androidx.compose.remote:remote-core:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-creation:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-creation-core:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-player-core:1.0.0-alpha06")
    implementation("androidx.compose.remote:remote-player-view:1.0.0-alpha06")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

tasks.register("pullDocument") {
    description =
        "Pull composable_button.rc from emulator into RachelRemoteComposeServer/documents/"
    group = "remote compose"
    doLast {
        val pkg = "io.github.hoaithu842.rachel.remotecompose.controller"
        val src = "/sdcard/Android/data/$pkg/files/composable_button.rc"
        val dest =
            "${rootProject.projectDir}/RachelRemoteComposeServer/documents/composable_button.rc"
        exec { commandLine("adb", "pull", src, dest) }
        println("Done → $dest")
    }
}
