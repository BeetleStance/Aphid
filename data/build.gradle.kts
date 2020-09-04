import com.beetlestance.buildsrc.Libs

plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

dependencies {

    // Local projects
    implementation(project(":base"))

    // Kotlin
    implementation(Libs.Kotlin.stdlib)
}