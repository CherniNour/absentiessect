plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.absentiessect1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.absentiessect1"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.poi) // For Apache POI
    implementation(libs.poi.ooxml)
    // Add Firebase libraries without specifying versions
    implementation(libs.firebase.auth)
    implementation(libs.firebase.store)

    // l Ajout  la dépendance Firebase Cloud Messaging (FCM)
    implementation("com.google.firebase:firebase-messaging")

    // Add MPAndroidChart dependency
    implementation(libs.mpandroidchart)  // This will now reference the MPAndroidChart library
    implementation(libs.volley)  // Reference to Volley


}
