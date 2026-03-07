plugins {
    id("ainote.android.library")
    id("ainote.android.room")
    id("ainote.android.hilt")
}

android {
    namespace = "com.ainote.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.coroutines.core)
}
