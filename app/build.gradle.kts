plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

// Dependency constraints for JavaPoet conflict resolution
configurations.all {
    resolutionStrategy {
        force("com.squareup:javapoet:1.13.0")
        eachDependency {
            if (requested.group == "com.google.dagger" && requested.name.startsWith("hilt")) {
                useVersion("2.52")
            }
            if (requested.group == "com.squareup" && requested.name == "javapoet") {
                useVersion("1.13.0")
                because("Force JavaPoet 1.13.0 for Hilt compatibility")
            }
        }
    }
}

android {
    namespace = "com.rekognizevote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rekognizevote"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        buildConfigField("String", "BASE_URL", "\"https://api.rekognizevote.com/\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://dev-api.rekognizevote.com/\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://api.rekognizevote.com/\"")
            
            // Signing config (configure keystore)
            // signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.lifecycle.viewmodel.compose)
    
    // Hilt - Using KAPT for stability
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    
    // Camera
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)
    
    // Image
    implementation(libs.coil.compose)
    implementation(libs.androidx.exifinterface)
    
    // Icons
    implementation(libs.compose.material.icons.extended)
    
    // Security
    implementation(libs.security.crypto)
    
    // Logging
    implementation(libs.timber)
    
    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    
    // Debug
    debugImplementation(libs.compose.ui.tooling)
}