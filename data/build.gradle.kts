plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.pavlovve.cybersporttracker.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

dependencies {

    api(project(":domain"))

    implementation(libs.gson)

    api(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.firebase.auth)
}
