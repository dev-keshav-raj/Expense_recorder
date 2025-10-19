plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "com.kr.expenserecoder"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kr.expenserecoder"
        minSdk = 24
        targetSdk = 36
        versionCode = 5
        versionName = "1.12.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
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
    implementation(libs.androidx.compiler)
    implementation(libs.androidx.navigation.compose.android)
//    implementation(libs.androidx.pdf.viewer)
    implementation(libs.androidx.webkit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.guava:guava:32.1.2-android")

    //room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Jetpack Compose
    implementation ("androidx.compose.ui:ui:1.5.1")
//    implementation ("androidx.compose.material3:material3:1.1.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.1")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.5.1")

    // Lifecycle + ViewModel
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // new one 
//    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // for gif
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-gif:2.4.0")

    // for swipe refresh
    // Jetpack Compose Material3
    implementation("androidx.compose.material3:material3:1.3.1")
//    implementation("androidx.compose.material3:material3-pullrefresh:1.3.1")
    
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")





}