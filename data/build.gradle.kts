plugins {
    id("ainote.android.library")
    id("ainote.android.hilt")
}

android {
    namespace = "com.ainote.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.core)
}
