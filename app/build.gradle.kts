plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id 'org.jetbrains.kotlin.kapt'
}

android {
    namespace = "com.example.learnandroid"
    // Android SDK version
    compileSdk = 34

//    dataBinding {
//        enabled = true
//    }

    defaultConfig {
        // appId
        applicationId = "com.example.learnandroid"
        // 最小SDK版本
        minSdk = 29
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

    // Dependency on local binaries
    // implementation fileTree(dir: 'libs', include: ['*.jar'])
    // navigation依赖
    implementation("androidx.navigation:navigation-fragment:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    // 引用本地commcon库
    implementation(project(":common_library"))
    // 其他模块
    implementation(project(":dashboard_module"))
    implementation(project(":my_module"))
    implementation(project(":home_module"))
}