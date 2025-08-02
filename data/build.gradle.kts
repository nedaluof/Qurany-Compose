plugins {
  alias(libs.plugins.com.android.library)
  alias(libs.plugins.org.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.nedaluof.data"
  compileSdk = Integer.valueOf(libs.versions.compile.sdk.get())

  defaultConfig {
    minSdk = Integer.valueOf(libs.versions.min.sdk.get())
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
    freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
  }
  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  //room database
  implementation(libs.bundles.room)
  ksp(libs.room.compiler)
  //hilt dependencies injection
  implementation(libs.hilt)
  ksp(libs.hilt.compiler)
  //MiHawk DataStore
  implementation(libs.mihawk)
  /** remote **/
  implementation(libs.retrofit) {
    exclude(module = "okhttp")
  }
  implementation(libs.retrofit.moshi.converter)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi)
  ksp(libs.moshi.kotlin.codegen)
  /** kotlin **/
  //Coroutines
  implementation(libs.coroutines)
  // debugging
  api(libs.timber)
  //testing
  testImplementation(libs.junit)
  androidTestImplementation(libs.junit.ext)
}