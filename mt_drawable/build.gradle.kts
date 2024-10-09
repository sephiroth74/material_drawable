plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("maven-publish")
}

val VERSION: String by project
val GROUP_ID: String by project

version = VERSION
group = GROUP_ID

android {
    compileSdk = 34
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation("androidx.core:core-ktx:1.13.1")
    testImplementation("junit:junit:4.13.2")
}


afterEvaluate {
    publishing {

        @Suppress("LocalVariableName")
        val GROUP_ID: String by project

        @Suppress("LocalVariableName")
        val VERSION: String by project

        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = GROUP_ID
                artifactId = "material_drawable"
                version = VERSION
            }
        }
    }
}
