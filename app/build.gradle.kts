plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Este plugin es esencial para Kotlin Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.tuempresa.gestionestudiantes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tuempresa.gestionestudiantes"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Navegación
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // --- Retrofit para llamadas HTTP ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Cambiado: Usamos el convertidor de Kotlin Serialization
    implementation("org.jetbrains.kotlinx:serialization-json:1.6.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Interceptor de Logs (Debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    // Corregido: 'corrigeimplementation' fue reemplazado por 'implementation'
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(libs.androidx.navigation.compose)
}