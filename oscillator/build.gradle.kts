plugins {
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.30")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                arguments("-DANDROID_STL=c++_shared")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.10.2"
        }
    }
    ndkVersion = "21.3.6528147"

    // Enable generation of Prefab packages and include them in the library's AAR.
    buildFeatures {
        prefabPublishing = true
    }

    // Include the "oscillator" module from the native build system in the AAR,
    // and export the headers in src/main/cpp/include to its consumers
    prefab {
        create("oscillator") {
           headers = "src/main/cpp/include"
        }
    }

    // Avoid packing the unnecessary libraries into final AAR. For details
    // refer to https://issuetracker.google.com/issues/168777344#comment5
    // Note that if your AAR also contains Java/Kotlin APIs, you should not
    // exclude libraries that are used by those APIs.
    packagingOptions {
        excludes.add("**/libmylibrary.so")
        excludes.add("**/libc++_shared.so")
    }
}
