plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger)
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    //Messaging
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)

}

android {
    namespace = "com.example.twitterfalso"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.twitterfalso"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.twitterfalso.HiltTestRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // 👇 Aquí pones el packaging
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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

    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.0") // fonts
    implementation("androidx.navigation:navigation-compose:2.7.2") // navigation
    implementation("androidx.compose.material:material-icons-extended:$2024.04.01")  // icons

    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    //Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.compose.navigation)
    implementation(libs.androidx.junit.ktx)
    kapt(libs.dagger.kapt)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation("com.google.firebase:firebase-storage-ktx")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Firestore
    implementation("com.google.firebase:firebase-firestore")

    //Messaging
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")

    //Ya viene incluida
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Adicionales
    testImplementation("io.mockk:mockk:1.13.11")
    //Courutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    //Alternativa a los asserts tradicionales
    testImplementation("com.google.truth:truth:1.4.2")
    androidTestImplementation("com.google.truth:truth:1.1.5")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.52")

    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    testImplementation(kotlin("test"))

    //e2e
    // AndroidX Test - Instrumented
    androidTestImplementation("androidx.test.ext:junit:1.1.52")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // ActivityScenario
    androidTestImplementation ("androidx.test:core:1.5.0")

    // Para UI / instrumented tests (androidTest/)
    androidTestImplementation("io.mockk:mockk-android:1.13.14")


}