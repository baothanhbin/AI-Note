plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    val libs = project.extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")
    add("implementation", libs.findLibrary("room.runtime").get())
    add("implementation", libs.findLibrary("room.ktx").get())
    add("ksp", libs.findLibrary("room.compiler").get())
}


