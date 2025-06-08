/*
 * Created By nedaluof  6/13/2020.
 * Updated By nedaluof  6/16/2024.
 */
plugins {
  alias(libs.plugins.com.android.application)
  alias(libs.plugins.org.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.google.services)
  alias(libs.plugins.firebase.crashlytics.plugin)
  alias(libs.plugins.org.jetbrains.kotlin.kapt)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.parcelize)
}

android {
  bundle {
    language {
      enableSplit = false
    }
  }
  namespace = "com.nedaluof.qurany"
  compileSdk = Integer.valueOf(libs.versions.compile.sdk.get())

  defaultConfig {
    applicationId = "com.nedaluof.qurany"
    minSdk = Integer.valueOf(libs.versions.min.sdk.get())
    targetSdk = Integer.valueOf(libs.versions.compile.sdk.get())
    versionCode = Integer.valueOf(libs.versions.version.code.get())
    versionName = libs.versions.version.name.get()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
    dataBinding = true
    buildConfig = true
  }
  kapt { correctErrorTypes = true }
}

dependencies {
  /** app **/
  //activity ktx
  implementation(libs.activity.ktx)
  //compose activity
  implementation(libs.compose.activity)
  //app compat
  implementation(libs.androidx.appcompat)
  //core extensions
  implementation(libs.core.ktx)
  //MDC
  implementation(libs.xml.material)
  //Compose
  implementation(platform(libs.compose.bom))
  androidTestImplementation(platform(libs.compose.bom))
  implementation(libs.bundles.compose)
  debugImplementation(libs.compose.ui.tooling.preview)
  androidTestImplementation(libs.compose.ui.test.junit4)
  debugImplementation(libs.compose.ui.test.manifest)
  //hilt navigation compose
  implementation(libs.hilt.navigation.compose)
  //compose nav component
  implementation(libs.compose.navigation)
  //material icons
  implementation(libs.androidx.material.icons.extended)
  //splash api
  implementation(libs.splash.api)
  //media3
  implementation(libs.bundles.media3)
  /**jet-pack Components**/
  //lifecycle
  implementation(libs.bundles.compose.lifecycle)
  //hilt dependencies injection
  implementation(libs.hilt)
  ksp(libs.hilt.compiler)
  /**Firebase**/
  implementation(platform(libs.firebase.bom))
  implementation(libs.bundles.firebase)
  /*data*/
  api(project(":data"))
  //Test libs
  testImplementation(libs.junit)
  androidTestImplementation(libs.bundles.test)
  testImplementation(libs.bundles.mockito)
}