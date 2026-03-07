plugins {
    id("ainote.android.feature")
}

android {
    namespace = "com.ainote.feature.editor"
}

dependencies {
    implementation(libs.androidx.compose.richtext.ui.material3)
    implementation(libs.androidx.compose.richtext.commonmark)
}
