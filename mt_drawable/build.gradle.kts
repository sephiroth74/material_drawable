plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("maven-publish")
}

group = "it.sephiroth.android.library"
version = "4.0.0"

android {
    compileSdk = 33
    namespace = "it.sephiroth.android.library.material.drawable"

    defaultConfig {
        minSdk = 24
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.core:core-ktx:1.9.0")

    testImplementation("junit:junit:4.13.2")
}

//
//afterEvaluate {
//    publishing {
//        publications {
//            release(MavenPublication) {
//                from = components.release
//                groupId = group
//                artifactId = 'material_drawable'
//                version = version
//            }
//        }
//    }
//}
