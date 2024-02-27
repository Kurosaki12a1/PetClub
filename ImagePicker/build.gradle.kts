plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.kien.imagepicker"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.arch.lifecycle)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.kotlinx.metadata.jvm)

    // Paging
    implementation(libs.paging.runtime)
}