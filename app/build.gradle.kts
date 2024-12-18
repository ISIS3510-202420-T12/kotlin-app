import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.wearabouts"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wearabouts"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Initialize properties object
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        // Set value part
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${properties.getProperty("SUPABASE_ANON_KEY")}\"")
        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("SUPABASE_URL")}\"")
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
        buildConfig = true
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
    val nav_version = "2.8.1"
    val activity_version = "1.9.2"
    val play_location = "18.0.0"
    val coroutines = "1.9.0"

    // Supabase
    implementation("io.github.jan-tennert.supabase:storage-kt:3.0.0")

    // Carrousel pages
    implementation("com.google.accompanist:accompanist-pager:0.23.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.23.1")

    // Android security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
    // Firestore with Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutines")

    // Import the BoM for the Firebase platform. When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database:20.2.2")

    // Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Material3
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("com.google.android.material:material:1.5.0")

    // Biometric handling
    implementation("androidx.biometric:biometric:1.1.0")

    // Permissions handling
    implementation("androidx.activity:activity-ktx:$activity_version")
    implementation("com.google.android.gms:play-services-location:$play_location")
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation ("io.coil-kt:coil:2.1.0")

    // MapBox API accesss
    val mapbox_version = "11.0.0"
    implementation("com.mapbox.maps:android:$mapbox_version")
    implementation("com.mapbox.extension:maps-compose:$mapbox_version")

    // Other core dependencies
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}