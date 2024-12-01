plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bagoesrex.storyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bagoesrex.storyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runner)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.androidx.cardview)

    dependencies {

        // Retrofit
        implementation (libs.retrofit)
        implementation (libs.converter.gson)
        implementation (libs.logging.interceptor)

        // Viewmodel
        implementation (libs.androidx.lifecycle.viewmodel.ktx)
        implementation (libs.androidx.lifecycle.livedata.ktx)
        implementation (libs.androidx.fragment.ktx)

        // DataStore
        implementation (libs.androidx.datastore.preferences)

        // Coroutines
        implementation (libs.kotlinx.coroutines.core)
        implementation (libs.kotlinx.coroutines.android)

        // Picasso
        implementation (libs.picasso)

        // CameraX
        implementation(libs.androidx.camera.camera2)
        implementation(libs.camera.lifecycle)
        implementation(libs.camera.view)
    }


}