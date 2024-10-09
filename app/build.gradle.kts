plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 34
    namespace = "it.sephiroth.android.app.app"

    defaultConfig {
        applicationId = "it.sephiroth.android.app.app"
        minSdk = 24
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        apiVersion = "1.9"
        languageVersion = "1.9"
        jvmTarget = "17"
    }

}

dependencies {
    implementation(project(":mt_drawable"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
