plugins {
    id("ainote.android.library")
    id("ainote.android.library.compose")
    id("ainote.android.hilt")
}

dependencies {
    val libs = project.extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

    add("implementation", project(":core:ui"))
    add("implementation", project(":core:designsystem"))
    add("implementation", project(":core:model"))
    add("implementation", project(":domain"))

    add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
    add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
    add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
    add("implementation", libs.findLibrary("androidx.navigation.compose").get())
    add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
    add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
}
