plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hajizade.lingualmate"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.hajizade.lingualmate"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //google fonts
    implementation(libs.androidx.compose.ui.text.google.fonts)

    //dragger hilt
    implementation("com.google.dagger:hilt-android:2.60.1")

    ksp("com.google.dagger:hilt-android-compiler:2.60.1")

    // Room Database
    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("androidx.navigation:navigation-compose:2.9.8")
    // تزریق ViewModelهای Hilt مستقیماً داخل Screens
    implementation("androidx.hilt:hilt-navigation-compose:1.4.0")

    // -------------------------------------------------------------
    // Networking (Retrofit & OkHttp)
    // -------------------------------------------------------------
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // -------------------------------------------------------------
    // Image Loading (Coil)
    // -------------------------------------------------------------
    // بارگذاری تصاویر محصولات از URL
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")

    implementation("androidx.compose.material:material-icons-core")
    // اگر به آیکون‌های بیشتری نیاز داشتی:
    implementation("androidx.compose.material:material-icons-extended")

    implementation("com.airbnb.android:lottie-compose:6.3.0")


    // استفاده از ماژول اصلی احراز هویت
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-firestore")
    implementation(platform("com.google.firebase:firebase-bom:34.18.0"))
    implementation("com.google.firebase:firebase-analytics")

    //credentials
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:34.18.0"))

    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")



}