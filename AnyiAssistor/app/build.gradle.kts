plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.anyiassistor"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.anyiassistor"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-code-scanner:16.0.0-eap1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs\\mysql-connector-java-5.1.47-bin.jar"))
    implementation(files("libs\\tencent-mapsdk-android-official-release.5.6.0.238fee88.jar"))
    //implementation("androidx.annotation:annotation-jvm:1.8.1")
    testImplementation("junit:junit:4.13.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")//OkHttp
    implementation("com.google.code.gson:gson:2.8.2")
    implementation( "androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")//Log工具
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}