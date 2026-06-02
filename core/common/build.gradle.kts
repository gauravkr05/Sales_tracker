plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.kinmin.core.common"
    compileSdk = 34
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(libs.coroutines.android)
}
