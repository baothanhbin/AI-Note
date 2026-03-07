plugins {
    id("ainote.android.library.compose")
}

android {
    namespace = "com.ainote.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
}
