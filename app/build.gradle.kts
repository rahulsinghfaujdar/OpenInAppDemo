import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.daggerHilt)
    kotlin(libs.versions.kapt.get())
    id(libs.versions.plugin.kotlinx.serialization.get())
}

android {
    namespace = "com.openinapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.openinapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(VERSION_1_8))
        }
    }
    kotlin {
        jvmToolchain(8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
        useBuildCache = false
        showProcessorStats = true
    }
    hilt {
        enableAggregatingTask = true
        enableTransformForLocalTests = true
        enableExperimentalClasspathAggregation = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.bundles.common)
    implementation(libs.bundles.lifecycle)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(files("libs/highcharts-android-10-0-1.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.bundles.retrofit)
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.swiperefreshlayout)

}