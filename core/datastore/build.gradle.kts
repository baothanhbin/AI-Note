plugins {
    id("ainote.android.library")
    id("ainote.android.hilt")
}

android {
    namespace = "com.ainote.core.datastore"
}

dependencies {
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
}
