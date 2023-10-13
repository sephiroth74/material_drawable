// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    `kotlin-dsl`
    groovy
    `java-gradle-plugin`
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

