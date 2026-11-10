plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.devtools.ksp")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.leapgesture"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.leapgesture"
        minSdk = 27
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
//    implementation(libs.androidx.constraintlayout)
//    implementation(libs.androidx.navigation.fragment.ktx)
//    implementation(libs.androidx.navigation.ui.ktx)
//    implementation(libs.androidx.activity)
    implementation(libs.firebase.database)
//    implementation(libs.firebase.auth)
//    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.tbuonomo:dotsindicator:4.3")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.material:material:1.12.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation ("androidx.activity:activity-ktx:1.7.2")

    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    // Shimmer Effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.google.android.material:material:1.8.0")

    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp ("androidx.room:room-compiler:2.5.2")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database:20.2.5")
    implementation ("com.google.firebase:firebase-auth:22.1.1")

    implementation("com.google.android.material:material:1.9.0")

    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore:24.4.3")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("com.google.firebase:firebase-database:20.1.0")
    // Firebase Realtime Database
    implementation ("com.google.firebase:firebase-database-ktx:20.2.2")

    // Firebase Storage
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.0")


    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("com.opencsv:opencsv:5.7.1") // Untuk membaca file CSV
    implementation ("com.squareup.picasso:picasso:2.8") // Untuk menampilkan gambar

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("com.microsoft.onnxruntime:onnxruntime-android:1.15.1")
}