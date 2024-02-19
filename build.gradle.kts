// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0-alpha16" apply false
    id("com.android.library") version "8.3.0-alpha16" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0-Beta4" apply false
    id("org.jetbrains.kotlin.jvm") version "2.0.0-Beta4" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

buildscript{
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}