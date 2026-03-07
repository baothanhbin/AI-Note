plugins {
    id("ainote.android.library")
}

android {
    namespace = "com.ainote.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}
