plugins {
    `kotlin-dsl`
}

group = "com.ainote.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.compose.compiler.plugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation("com.squareup:javapoet:1.13.0")
}
