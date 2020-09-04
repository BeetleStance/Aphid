import com.beetlestance.buildsrc.Libs

plugins {
    id("java-library")
    id("kotlin")
    id("com.android.lint")
}

lintOptions {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    isAbsolutePaths = false
    isIgnoreTestSources = true
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

dependencies {

    // Kotlin
    compileOnly(Libs.Kotlin.jdk7)

    compileOnly(Libs.Lint.api)
    compileOnly(Libs.Lint.checks)
}
