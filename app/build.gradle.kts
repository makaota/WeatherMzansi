 plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.makaota.weathermzansi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.makaota.weathermzansi"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Dagger - Hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp (libs.androidx.hilt.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    // Location Services
    implementation (libs.play.services.location)

    // Google Places API
    implementation (libs.places)

    // Google Maps SDK
    implementation (libs.play.services.maps)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.moshi)
    implementation(libs.logging.interceptor)

    // Accompanist
    implementation (libs.accompanist.swiperefresh)
    implementation (libs.accompanist.systemuicontroller)
    

    implementation(libs.androidx.room.runtime)

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)

    // Preferences DataStore (SharedPreferences like APIs)
    
    implementation(libs.androidx.datastore.preferences)

        // optional - RxJava2 support
    implementation(libs.androidx.datastore.preferences.rxjava2)

        // optional - RxJava3 support
    implementation(libs.androidx.datastore.preferences.rxjava3)


    // Alternatively - use the following artifact without an Android dependency.

    implementation(libs.androidx.datastore.preferences.core)


    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Lifecycle utilities for Compose
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)












}