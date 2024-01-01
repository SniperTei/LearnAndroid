plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id 'org.jetbrains.kotlin.kapt'
}

android {
    namespace = "com.example.learnandroid"
    // Android SDK version
    compileSdk = 33

//    dataBinding {
//        enabled = true
//    }

    defaultConfig {
        // appId
        applicationId = "com.example.learnandroid"
        // 最小SDK版本
        minSdk = 24
        // 目标版本
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // 构建类型
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
    }
//    kotlin {
//        jvmToolchain(8)
//    }
}

dependencies {

//    implementation 'androidx.core:core-ktx:1.8.0'
//    implementation 'androidx.appcompat:appcompat:1.6.1'
//    implementation 'com.google.android.material:material:1.5.0'
//    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
//    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    // Dependency on a local library module
    // implementation project(':mylibrary')
    // Dependency on local binaries
    // implementation fileTree(dir: 'libs', include: ['*.jar'])
    // 添加RecyclerView的依赖包
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // 添加ViewPage2的依赖包
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // navigation依赖
    implementation("androidx.navigation:navigation-fragment:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    // Rx依赖
//    implementation 'io.reactivex.rxjava3:rxandroid:3.0.2'
//    implementation 'io.reactivex.rxjava3:rxjava:3.1.5'
    // banner依赖
    implementation("io.github.youth5201314:banner:2.2.2")
    // 引用本地commcon库
//    implementation project(":common_module")
}