plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.common_module"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    api("androidx.core:core-ktx:1.9.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // glide依赖
    api("com.github.bumptech.glide:glide:4.16.0")
//    kapt 'com.github.bumptech.glide:compiler:4.16.0'
    // okhttp依赖
    api("com.squareup.okhttp3:okhttp:4.11.0")
    // retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.retrofit2:adapter-rxjava3:2.9.0") // 添加RxJava3适配器
    // gson ： com.google.code.gson命名空间的gson库的2.10.1版本
    api("com.google.code.gson:gson:2.10.1")
    // lifeCycle
    val lifecycleVersion = "2.2.0"
    api("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // 可选 - ProcessLifecycleOwner给整个 app进程 提供一个lifecycle
//    api("androidx.lifecycle:lifecycle-process:$lifecycleVersion")
    // kunminx
    implementation("com.kunminx.arch:unpeek-livedata:7.8.0")
}