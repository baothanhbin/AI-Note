plugins {
    id("ainote.android.library")
    id("ainote.android.hilt")
}

android {
    namespace = "com.ainote.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":data"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.android)
}
