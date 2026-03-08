plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

dependencies {
    val libs = project.extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")
    add("implementation", libs.findLibrary("hilt.android").get())
    add("ksp", libs.findLibrary("hilt.compiler").get())
}
