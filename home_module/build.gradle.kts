plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.home_module"
    compileSdk = 33

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        kapt {
            arguments {
                // 根据模块名来命名路由根节点
                arg("AROUTER_MODULE_NAME", project.name)
                // 生成JSON文件
                arg("AROUTER_GENERATE_DOC", "enable")
            }
        }
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

implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // 添加RecyclerView的依赖包
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // 添加ViewPage2的依赖包
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // banner依赖
    implementation("io.github.youth5201314:banner:2.2.2")

    implementation(project(":common_library"))
    implementation(project(":core_library"))
    val arouterVersion = "1.5.2"
    kapt("com.alibaba:arouter-compiler:$arouterVersion")
}