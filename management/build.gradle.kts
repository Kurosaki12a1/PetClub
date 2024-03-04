plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.kien.petclub"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }


    hilt {
        enableAggregatingTask = true
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/java")
        getByName("test").java.srcDir("src/test/java")
        getByName("androidTest").java.srcDir("src/androidTest/java")
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

}

dependencies {
    implementation(project(":ImagePicker"))
    implementation(project(":flexbox"))

    // AndroidX component libraries
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)

    // Navigation
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.kotlinx.metadata.jvm)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.arch.lifecycle)

    // Lottie Animation
    implementation(libs.lottie)

    // Google Services
    implementation(libs.play.services.auth)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.21-1.0.15")

    // Scan BarCode
    implementation(libs.zxing.android.embedded)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)

    // Test Implementation
    // Unit Test
    testImplementation(libs.mockK)
    testImplementation(libs.junit)
    testImplementation(libs.roboletric)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.core.testing)

    // Android Test
    testImplementation(libs.androidx.navigation.testing)

}